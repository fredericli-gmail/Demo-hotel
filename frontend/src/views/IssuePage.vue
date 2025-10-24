<template>
  <!-- 繁體中文註解：發卡表單，協助櫃檯輸入房客資訊並送出發卡請求。 -->
  <div class="container">
    <section class="card">
      <h2>發卡作業</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-row">
          <label for="vcUid">VC 模板代碼（credentialType）</label>
          <input
            id="vcUid"
            v-model="form.vcUid"
            required
            placeholder="請輸入 credentialType，例如：00000000_hlrc1023"
          />
        </div>
        <div class="form-row">
          <label for="roomNb">房間號碼</label>
          <input id="roomNb" v-model="form.roomNb" required />
        </div>
        <div class="form-row">
          <label for="roomType">房間型態</label>
          <input id="roomType" v-model="form.roomType" placeholder="選填，例如：雙人房" />
        </div>
        <div class="form-row">
          <label for="roomMemo">備註</label>
          <input id="roomMemo" v-model="form.roomMemo" placeholder="選填，例如：需加床" />
        </div>
        <div class="form-row">
          <label for="checkInDate">入住日期</label>
          <input id="checkInDate" type="date" v-model="form.checkInDate" @change="handleCheckInChange" required />
        </div>
        <div class="form-row">
          <label for="checkOutDate">退房日期</label>
          <input id="checkOutDate" type="date" v-model="form.checkOutDate" @change="handleCheckOutChange" required />
        </div>
        <div class="form-row">
          <label for="dataTag">資料標籤</label>
          <input id="dataTag" v-model="form.dataTag" placeholder="可自訂標籤方便追蹤" />
        </div>
        <button class="button" type="submit" :disabled="loading">{{ loading ? '處理中...' : '送出發卡' }}</button>
      </form>
    </section>

    <section v-if="result" class="card">
      <h3>發卡結果</h3>
      <p><strong>交易序號：</strong>{{ result.transactionId }}</p>
      <p><strong>Deep Link：</strong>{{ result.deepLink }}</p>
      <div v-if="result.qrCode" class="qr-wrapper">
        <img :src="result.qrCode" alt="房卡 QR Code" />
      </div>
    </section>
  </div>
</template>

<script>
import { issueCredential } from '../services/credentialService';

export default {
  name: 'IssuePage',
  data() {
    return {
      form: {
        vcUid: '00000000_hlrc1023',
        roomNb: '',
        roomType: '',
        roomMemo: '',
        checkInDate: '',
        checkOutDate: '',
        dataTag: ''
      },
      loading: false,
      result: null
    };
  },
  created() {
    // 繁體中文註解：預設入住日為今日，退房日為隔日，方便櫃檯快速操作。
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.form.checkInDate = this.formatDateForInput(today);
    this.form.checkOutDate = this.formatDateForInput(tomorrow);
  },
  methods: {
    formatDateForInput(date) {
      const year = date.getFullYear();
      const month = `${date.getMonth() + 1}`.padStart(2, '0');
      const day = `${date.getDate()}`.padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    formatDateForPayload(value) {
      return value.replace(/-/g, '');
    },
    normalizeOptional(value) {
      if (!value) {
        return undefined;
      }
      const trimmed = value.trim();
      return trimmed.length > 0 ? trimmed : undefined;
    },
    handleCheckInChange() {
      if (!this.form.checkInDate) {
        return;
      }
      const checkIn = new Date(this.form.checkInDate);
      if (isNaN(checkIn.getTime())) {
        return;
      }
      const checkOut = this.form.checkOutDate ? new Date(this.form.checkOutDate) : null;
      if (!checkOut || isNaN(checkOut.getTime()) || checkOut <= checkIn) {
        const nextDay = new Date(checkIn);
        nextDay.setDate(nextDay.getDate() + 1);
        this.form.checkOutDate = this.formatDateForInput(nextDay);
      }
    },
    handleCheckOutChange() {
      if (!this.form.checkInDate) {
        return;
      }
      const checkIn = new Date(this.form.checkInDate);
      if (isNaN(checkIn.getTime())) {
        return;
      }
      const checkOut = new Date(this.form.checkOutDate);
      if (isNaN(checkOut.getTime())) {
        const nextDay = new Date(checkIn);
        nextDay.setDate(nextDay.getDate() + 1);
        this.form.checkOutDate = this.formatDateForInput(nextDay);
        return;
      }
      if (checkOut <= checkIn) {
        const nextDay = new Date(checkIn);
        nextDay.setDate(nextDay.getDate() + 1);
        this.form.checkOutDate = this.formatDateForInput(nextDay);
        window.alert('退房日期必須晚於入住日期，系統已自動調整為隔日。');
      }
    },
    async handleSubmit() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        const payload = {
          vcUid: this.form.vcUid.trim(),
          roomNb: this.form.roomNb.trim(),
          roomType: this.normalizeOptional(this.form.roomType),
          roomMemo: this.normalizeOptional(this.form.roomMemo),
          checkInDate: this.formatDateForPayload(this.form.checkInDate),
          checkOutDate: this.formatDateForPayload(this.form.checkOutDate),
          dataTag: this.normalizeOptional(this.form.dataTag)
        };
        if (!payload.roomType) {
          delete payload.roomType;
        }
        if (!payload.roomMemo) {
          delete payload.roomMemo;
        }
        if (!payload.dataTag) {
          delete payload.dataTag;
        }
        const response = await issueCredential(payload);
        this.result = response;
      } catch (error) {
        window.alert(error.message);
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.form-row {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 16px;
}

.form-row input {
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #cbd5e1;
}

.qr-wrapper {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.qr-wrapper img {
  width: 200px;
  height: 200px;
}
</style>

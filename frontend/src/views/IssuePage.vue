<template>
  <!-- 繁體中文註解：發卡表單，協助櫃檯輸入房客資訊並送出發卡請求。 -->
  <div class="container">
    <section class="card">
      <header class="section-heading">
        <div>
          <h2 class="section-title">發卡作業</h2>
          <p class="section-subtitle">輸入旅客資訊即可立即產出專屬 VC 房卡並提供掃描。</p>
        </div>
      </header>
      <form class="form-grid" @submit.prevent="handleSubmit">
        <div class="form-field form-field--full">
          <label for="vcUid">VC 模板代碼（credentialType）</label>
          <input
            id="vcUid"
            v-model="form.vcUid"
            required
            readonly
            placeholder="請輸入 credentialType，例如：00000000_hlrc1023"
          />
          <p class="helper-text">系統固定使用房卡模板 `00000000_hlrc1023`，如需更換請洽管理人員調整設定。</p>
        </div>
        <div class="form-field">
          <label for="roomNb">房間號碼</label>
          <input id="roomNb" v-model="form.roomNb" required />
        </div>
        <div class="form-field">
          <label for="roomType">房間型態</label>
          <input id="roomType" v-model="form.roomType" placeholder="選填，例如：雙人房" />
        </div>
        <div class="form-field">
          <label for="roomMemo">備註</label>
          <input id="roomMemo" v-model="form.roomMemo" placeholder="選填，例如：需加床" />
        </div>
        <div class="form-field">
          <label for="checkInDate">入住日期</label>
          <input id="checkInDate" type="date" v-model="form.checkInDate" @change="handleCheckInChange" required />
        </div>
        <div class="form-field">
          <label for="checkOutDate">退房日期</label>
          <input id="checkOutDate" type="date" v-model="form.checkOutDate" @change="handleCheckOutChange" required />
        </div>
        <div class="form-field form-field--full">
          <label for="dataTag">資料標籤</label>
          <input id="dataTag" v-model="form.dataTag" placeholder="可自訂批次或用途，選填" />
        </div>
        <div class="form-actions">
          <button class="button" type="submit" :disabled="loading">{{ loading ? '處理中...' : '送出發卡' }}</button>
        </div>
      </form>
    </section>

    <qr-modal
      :visible="showResultModal"
      title="房卡已成功產出"
      description="請現場掃描 QR Code 或點選 Deep Link，完成 VC 領取。"
      :result="result"
      @close="handleModalClose"
    />
  </div>
</template>

<script>
import { issueCredential } from '../services/credentialService';
import QrModal from '../components/qr-modal.vue';

export default {
  name: 'IssuePage',
  components: { QrModal },
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
      result: null,
      showResultModal: false
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
        this.showResultModal = true;
      } catch (error) {
        window.alert(error.message);
      } finally {
        this.loading = false;
      }
    },
    handleModalClose() {
      this.showResultModal = false;
    }
  }
};
</script>

<style scoped>
.section-heading {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 28px;
}

.form-grid {
  display: grid;
  gap: 20px 26px;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.form-field--full {
  grid-column: 1 / -1;
}

.helper-text {
  margin: 0;
  font-size: 12px;
  color: var(--color-text-muted);
}

.form-actions {
  grid-column: 1 / -1;
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}
</style>

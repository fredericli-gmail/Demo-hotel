<template>
  <!-- 繁體中文註解：早餐券發放表單，提供櫃檯輸入房號等資訊。 -->
  <div class="container">
    <section class="card">
      <h2>早餐券發放</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-row">
          <label for="vcUid">VC 模板代碼（credentialType）</label>
          <input
            id="vcUid"
            v-model="form.vcUid"
            required
            placeholder="請輸入 credentialType，例如：00000000_hlbft1023"
          />
        </div>
        <div class="form-row">
          <label for="roomNb">房間號碼</label>
          <input id="roomNb" v-model="form.roomNb" required />
        </div>
        <div class="form-row">
          <label for="ticketType">類別</label>
          <input id="ticketType" v-model="form.ticketType" placeholder="選填，例如：成人餐券" />
        </div>
        <div class="form-row">
          <label for="location">餐廳位置</label>
          <input id="location" v-model="form.location" placeholder="選填，例如：一樓自助餐廳" />
        </div>
        <div class="form-row">
          <label for="validDate">使用日期</label>
          <input id="validDate" type="date" v-model="form.validDate" required />
        </div>
        <div class="form-row">
          <label for="dataTag">資料標籤</label>
          <input id="dataTag" v-model="form.dataTag" placeholder="可記錄用途或批次" />
        </div>
        <button class="button" type="submit" :disabled="loading">{{ loading ? '處理中...' : '發放早餐券' }}</button>
      </form>
    </section>

    <section v-if="result" class="card">
      <h3>發券結果</h3>
      <p><strong>交易序號：</strong>{{ result.transactionId }}</p>
      <p><strong>Deep Link：</strong>{{ result.deepLink }}</p>
      <div v-if="result.qrCode" class="qr-wrapper">
        <img :src="result.qrCode" alt="早餐券 QR Code" />
      </div>
    </section>
  </div>
</template>

<script>
import { issueBreakfastTicket } from '../services/credentialService';

export default {
  name: 'BreakfastTicketPage',
  data() {
    return {
      form: {
        vcUid: '00000000_hlbft1023',
        roomNb: '',
        ticketType: '',
        location: '',
        validDate: '',
        dataTag: ''
      },
      loading: false,
      result: null
    };
  },
  created() {
    // 繁體中文註解：早餐券預設使用日期為今日，方便櫃檯快速發放。
    const today = new Date();
    this.form.validDate = this.formatDateForInput(today);
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
    async handleSubmit() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        const payload = {
          vcUid: this.form.vcUid.trim(),
          roomNb: this.form.roomNb.trim(),
          ticketType: this.normalizeOptional(this.form.ticketType),
          location: this.normalizeOptional(this.form.location),
          validDate: this.formatDateForPayload(this.form.validDate),
          dataTag: this.normalizeOptional(this.form.dataTag)
        };
        if (!payload.ticketType) {
          delete payload.ticketType;
        }
        if (!payload.location) {
          delete payload.location;
        }
        if (!payload.dataTag) {
          delete payload.dataTag;
        }
        const response = await issueBreakfastTicket(payload);
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

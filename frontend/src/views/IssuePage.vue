<template>
  <!-- 繁體中文註解：發卡表單，協助櫃檯輸入房客資訊並送出發卡請求。 -->
  <div class="container">
    <section class="card">
      <h2>發卡作業</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-row">
          <label for="vcUid">VC 模板代碼</label>
          <input id="vcUid" v-model="form.vcUid" required />
        </div>
        <div class="form-row">
          <label for="guestName">房客姓名</label>
          <input id="guestName" v-model="form.guestName" required />
        </div>
        <div class="form-row">
          <label for="roomNumber">房號</label>
          <input id="roomNumber" v-model="form.roomNumber" required />
        </div>
        <div class="form-row">
          <label for="checkInDate">入住日期 (YYYYMMDD)</label>
          <input id="checkInDate" v-model="form.checkInDate" required />
        </div>
        <div class="form-row">
          <label for="checkOutDate">退房日期 (YYYYMMDD)</label>
          <input id="checkOutDate" v-model="form.checkOutDate" required />
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
        vcUid: '',
        guestName: '',
        roomNumber: '',
        checkInDate: '',
        checkOutDate: '',
        dataTag: ''
      },
      loading: false,
      result: null
    };
  },
  methods: {
    async handleSubmit() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        const payload = {
          vcUid: this.form.vcUid,
          guestName: this.form.guestName,
          roomNumber: this.form.roomNumber,
          checkInDate: this.form.checkInDate,
          checkOutDate: this.form.checkOutDate,
          dataTag: this.form.dataTag
        };
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

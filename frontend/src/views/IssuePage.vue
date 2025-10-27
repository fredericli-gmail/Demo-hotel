<template>
  <!-- 繁體中文註解：發卡表單，協助櫃檯輸入房客資訊並送出發卡請求。 -->
  <div class="container">
    <section class="card verification-card">
      <header class="section-heading">
        <div>
          <h2 class="section-title">駕照驗證</h2>
          <p class="section-subtitle">請使用 https://demo.wallet.gov.tw/getcard/ 來領取駕照電子卡。</p>
          <p class="section-subtitle">請旅客開啟數位憑證皮夾掃描 QR Code，完成身分驗證後再進行發卡。</p>
        </div>
        <div class="verification-actions">
          <button class="button" type="button" :disabled="driverVerification.loading" @click="initializeDriverVerification">
            {{ driverVerification.transactionId ? '重新產生驗證 QR' : '產生驗證 QR' }}
          </button>
        </div>
      </header>
      <div class="verification-body">
        <div class="verification-qr">
          <div class="qr-wrapper" v-if="driverVerification.qrcodeImage">
            <img :src="driverVerification.qrcodeImage" alt="Driver License Verification QR Code" />
          </div>
          <div class="qr-placeholder" v-else>
            <span>尚未產生 QR Code</span>
          </div>
          <p class="verification-countdown" v-if="driverVerification.status === 'WAITING'">
            驗證倒數：{{ countdownText }}
          </p>
          <p class="verification-countdown" v-else>
            {{ driverVerification.message }}
          </p>
          <button
            v-if="driverVerification.authUri"
            type="button"
            class="link-button"
            @click="openAuthUri"
          >
            使用手機開啟
          </button>
        </div>
        <div class="verification-result">
          <h3>驗證結果</h3>
          <template v-if="driverVerification.verified">
            <p class="verification-status success">已完成驗證</p>
            <ul class="claim-list">
              <li v-for="item in driverVerification.claims" :key="item.ename">
                <span class="claim-label">{{ item.label }}</span>
                <span class="claim-value">{{ item.value || '－' }}</span>
              </li>
            </ul>
          </template>
          <template v-else>
            <p :class="['verification-status', driverVerification.status === 'FAILED' ? 'error' : 'pending']">
              {{ driverVerification.message }}
            </p>
            <ul v-if="driverVerification.claims.length" class="claim-list">
              <li v-for="item in driverVerification.claims" :key="item.ename">
                <span class="claim-label">{{ item.label }}</span>
                <span class="claim-value">{{ item.value || '－' }}</span>
              </li>
            </ul>
          </template>
          <div v-if="driverVerification.transactionId" class="transaction-id">
            交易序號：{{ driverVerification.transactionId }}
          </div>
        </div>
      </div>
    </section>

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
import { createDriverLicenseVerification, getDriverLicenseVerificationResult } from '../services/driverLicenseVerificationService';
import QrModal from '../components/qr-modal.vue';

const DRIVER_VERIFICATION_POLL_INTERVAL = 3000;

export default {
  name: 'IssuePage',
  components: { QrModal },
  data() {
    return {
      form: {
        vcUid: '2-16-886-1-101-90001-20004-30004-30004-40004_hlrc1023',
        roomNb: '',
        roomType: '',
        roomMemo: '',
        checkInDate: '',
        checkOutDate: '',
        dataTag: ''
      },
      loading: false,
      result: null,
      showResultModal: false,
      driverVerification: {
        loading: false,
        transactionId: null,
        qrcodeImage: null,
        authUri: null,
        expiresIn: 0,
        countdown: 0,
        status: 'IDLE',
        message: '請產生驗證 QR Code，並請旅客使用數位憑證皮夾掃描。',
        claims: [],
        verified: false,
        timerId: null,
        pollingId: null
      }
    };
  },
  computed: {
    countdownText() {
      const total = this.driverVerification.countdown;
      const minutes = Math.floor(total / 60)
        .toString()
        .padStart(2, '0');
      const seconds = Math.max(total % 60, 0)
        .toString()
        .padStart(2, '0');
      return `${minutes}:${seconds}`;
    }
  },
  created() {
    // 繁體中文註解：預設入住日為今日，退房日為隔日，方便櫃檯快速操作。
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.form.checkInDate = this.formatDateForInput(today);
    this.form.checkOutDate = this.formatDateForInput(tomorrow);
    this.initializeDriverVerification();
  },
  beforeUnmount() {
    this.clearDriverVerificationTimers(true);
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
    async initializeDriverVerification() {
      this.clearDriverVerificationTimers(true);
      this.driverVerification.loading = true;
      try {
        const response = await createDriverLicenseVerification();
        this.driverVerification.transactionId = response.transactionId;
        this.driverVerification.qrcodeImage = response.qrcodeImage;
        this.driverVerification.authUri = response.authUri;
        this.driverVerification.expiresIn = response.expiresIn || 300;
        this.driverVerification.countdown = this.driverVerification.expiresIn;
        this.driverVerification.status = 'WAITING';
        this.driverVerification.message = '請旅客開啟數位憑證皮夾掃描 QR Code 完成驗證。';
        this.driverVerification.claims = [];
        this.driverVerification.verified = false;
        this.startDriverVerificationTimers();
      } catch (error) {
        const message = error && error.message ? error.message : '驗證 QR 產生失敗，請稍後再試。';
        this.driverVerification.status = 'ERROR';
        this.driverVerification.message = message;
      } finally {
        this.driverVerification.loading = false;
      }
    },
    startDriverVerificationTimers() {
      this.clearDriverVerificationTimers(false);
      this.driverVerification.timerId = window.setInterval(() => {
        if (this.driverVerification.countdown > 0) {
          this.driverVerification.countdown -= 1;
        } else {
          this.driverVerification.status = 'EXPIRED';
          this.driverVerification.message = '驗證倒數已結束，請重新產生驗證 QR。';
          this.clearDriverVerificationTimers(false);
        }
      }, 1000);

      this.driverVerification.pollingId = window.setInterval(() => {
        this.fetchDriverLicenseResult();
      }, DRIVER_VERIFICATION_POLL_INTERVAL);
    },
    clearDriverVerificationTimers(resetState) {
      if (this.driverVerification.timerId) {
        window.clearInterval(this.driverVerification.timerId);
        this.driverVerification.timerId = null;
      }
      if (this.driverVerification.pollingId) {
        window.clearInterval(this.driverVerification.pollingId);
        this.driverVerification.pollingId = null;
      }
      if (resetState) {
        this.driverVerification.transactionId = null;
        this.driverVerification.qrcodeImage = null;
        this.driverVerification.authUri = null;
        this.driverVerification.countdown = 0;
        this.driverVerification.expiresIn = 0;
        this.driverVerification.claims = [];
        this.driverVerification.status = 'IDLE';
        this.driverVerification.message = '請產生驗證 QR Code，並請旅客使用數位憑證皮夾掃描。';
        this.driverVerification.verified = false;
      }
    },
    async fetchDriverLicenseResult() {
      if (!this.driverVerification.transactionId || this.driverVerification.status !== 'WAITING') {
        return;
      }
      try {
        const response = await getDriverLicenseVerificationResult(this.driverVerification.transactionId);
        this.driverVerification.status = response.status || 'WAITING';
        this.driverVerification.message = response.message || this.driverVerification.message;
        this.driverVerification.claims = response.claims || [];
        this.driverVerification.verified = !!response.verified;

        if (this.driverVerification.verified || this.driverVerification.status === 'FAILED') {
          this.clearDriverVerificationTimers(false);
        }
      } catch (error) {
        console.error('查詢駕照驗證結果失敗', error);
      }
    },
    openAuthUri() {
      if (this.driverVerification.authUri) {
        window.open(this.driverVerification.authUri, '_blank');
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
.verification-card {
  margin-bottom: 32px;
}

.section-heading {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 28px;
}

.verification-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.verification-body {
  display: flex;
  flex-wrap: wrap;
  gap: 32px;
}

.verification-qr {
  flex: 1 1 260px;
  max-width: 320px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.qr-wrapper {
  width: 220px;
  height: 220px;
  border-radius: 20px;
  border: 1px solid rgba(148, 163, 184, 0.2);
  box-shadow: 0 14px 32px rgba(15, 23, 42, 0.18);
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.9);
}

.qr-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.qr-placeholder {
  width: 220px;
  height: 220px;
  border-radius: 20px;
  border: 1px dashed rgba(148, 163, 184, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-muted);
  font-size: 14px;
  text-align: center;
  padding: 24px;
}

.verification-countdown {
  font-size: 14px;
  color: var(--color-text-muted);
  margin: 0;
}

.link-button {
  border: none;
  background: transparent;
  color: var(--color-primary);
  cursor: pointer;
  font-size: 14px;
  text-decoration: underline;
}

.verification-result {
  flex: 1 1 320px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.verification-result h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #0f172a;
}

.verification-status {
  margin: 0;
  font-size: 14px;
}

.verification-status.success {
  color: #0f9d58;
}

.verification-status.error {
  color: #ef4444;
}

.verification-status.pending {
  color: var(--color-text-muted);
}

.claim-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  gap: 10px 18px;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
}

.claim-list li {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 14px 16px;
  border-radius: 14px;
  background: rgba(15, 23, 42, 0.04);
}

.claim-label {
  font-size: 12px;
  text-transform: uppercase;
  color: rgba(15, 23, 42, 0.55);
  letter-spacing: 0.08em;
}

.claim-value {
  font-size: 15px;
  font-weight: 600;
  color: #0f172a;
}

.transaction-id {
  font-size: 12px;
  color: var(--color-text-muted);
}

@media (max-width: 768px) {
  .verification-body {
    flex-direction: column;
  }

  .verification-qr {
    align-items: flex-start;
  }

  .qr-wrapper,
  .qr-placeholder {
    width: 200px;
    height: 200px;
  }
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

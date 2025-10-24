<template>
  <!-- 繁體中文註解：早餐券發放頁面，採用與房卡一致的互動體驗。 -->
  <div class="container">
    <section class="card">
      <header class="section-heading">
        <div>
          <h2 class="section-title">早餐券發放</h2>
          <p class="section-subtitle">快速生成旅客專屬早餐券，可自訂類別與用餐地點。</p>
        </div>
      </header>
      <form class="form-grid" @submit.prevent="handleSubmit">
        <div class="form-field form-field--full">
          <label for="vcUid">VC 模板代碼（credentialType）</label>
          <input
            id="vcUid"
            v-model="form.vcUid"
            required
            placeholder="請輸入 credentialType，例如：00000000_hlbft1023"
          />
          <p class="helper-text">預設使用早餐券模板 `00000000_hlbft1023`，如需異動請先確認欄位定義。</p>
        </div>
        <div class="form-field">
          <label for="roomNb">房間號碼</label>
          <input id="roomNb" v-model="form.roomNb" required />
        </div>
        <div class="form-field">
          <label for="ticketType">類別</label>
          <input id="ticketType" v-model="form.ticketType" placeholder="選填，例如：成人餐券" />
        </div>
        <div class="form-field">
          <label for="location">餐廳位置</label>
          <input id="location" v-model="form.location" placeholder="選填，例如：一樓自助餐廳" />
        </div>
        <div class="form-field">
          <label for="validDate">使用日期</label>
          <input id="validDate" type="date" v-model="form.validDate" required />
        </div>
        <div class="form-field form-field--full">
          <label for="dataTag">資料標籤</label>
          <input id="dataTag" v-model="form.dataTag" placeholder="可記錄批次或活動名稱，選填" />
        </div>
        <div class="form-actions">
          <button class="button" type="submit" :disabled="loading">{{ loading ? '處理中...' : '發放早餐券' }}</button>
        </div>
      </form>
    </section>

    <qr-modal
      :visible="showResultModal"
      title="早餐券已發放"
      description="掃描或開啟 Deep Link，旅客可立即領取早餐券。"
      :result="result"
      @close="handleModalClose"
    />
  </div>
</template>

<script>
import { issueBreakfastTicket } from '../services/credentialService';
import QrModal from '../components/qr-modal.vue';

export default {
  name: 'BreakfastTicketPage',
  components: { QrModal },
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
      result: null,
      showResultModal: false
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

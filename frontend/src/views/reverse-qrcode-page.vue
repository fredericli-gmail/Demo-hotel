<template>
  <div class="container">
    <section class="card">
      <header class="section-heading">
        <div>
          <h2 class="section-title">驗證房卡/票券</h2>
          <p class="section-subtitle">貼上加密字串即可檢視房卡或早餐券資訊，必要時支援憑證撤銷。</p>
        </div>
      </header>
      <form class="form-grid" @submit.prevent="handleDecode">
        <div class="form-field form-field--full">
          <label for="encryptedData">加密字串</label>
          <textarea
            id="encryptedData"
            v-model="form.encryptedData"
            class="encrypted-input"
            placeholder="請貼上加密資料，例如：{&quot;d&quot;:&quot;...&quot;,&quot;h&quot;:&quot;...&quot;}"
            required
          ></textarea>
          <p class="helper-text">支援 d/h 結構與純 Base64 內容，系統會自動判斷格式。</p>
        </div>

        <div class="form-actions">
          <button class="button" type="submit" :disabled="loading">
            {{ loading ? '解析中...' : '開始解析' }}
          </button>
        </div>
      </form>
    </section>

    <section class="card" v-if="result">
      <header class="section-heading">
        <h3 class="section-title">解析結果</h3>
        <p class="section-subtitle" :class="result.success ? 'text-success' : 'text-error'">{{ result.message }}</p>
      </header>
      <div class="result-block">
        <p class="result-status" :class="result.success ? 'success' : 'error'">
          {{ result.success ? '驗證通過' : '驗證失敗' }}
        </p>
        <div v-if="revokeCandidate" class="revoke-section">
          <h4>{{ revokeTitle || '偵測到可撤銷的憑證' }}</h4>
          <ul>
            <li v-for="item in revokeDetails" :key="item.label">
              <strong>{{ item.label }}：</strong>{{ item.value }}
            </li>
          </ul>
          <div class="revoke-actions">
            <button class="button button--danger" type="button" :disabled="revoking || !revokeCandidate.cid" @click="handleRevoke">
              {{ revoking ? '處理中...' : '使用' }}
            </button>
          </div>
        </div>
        <p v-if="revokeMessage" class="revoke-message">{{ revokeMessage }}</p>
        <div v-if="result.data" class="result-grid">
          <div v-for="(value, key) in result.data" :key="key" class="result-item">
            <span class="result-key">{{ key }}</span>
            <span class="result-value">{{ formatValue(value) }}</span>
          </div>
        </div>
        <div v-if="result.decryptedPayload" class="result-json">
          <label>原始 JSON</label>
          <pre>{{ prettyJson(result.decryptedPayload) }}</pre>
        </div>
      </div>
    </section>

    <section class="card" v-else-if="error">
      <header class="section-heading">
        <h3 class="section-title">解析錯誤</h3>
      </header>
      <p class="error-message">{{ error }}</p>
    </section>
  </div>
</template>

<script>
import { decodeQrCode } from '../services/reverseQrService';
import { revokeCredential, lookupCredentialForRevoke } from '../services/credentialService';

export default {
  name: 'ReverseQrcodePage',
  data() {
    return {
      form: {
        encryptedData: ''
      },
      loading: false,
      result: null,
      error: null,
      originalMeta: null,
      revokeCandidate: null,
      revokeTitle: '',
      revoking: false,
      revokeMessage: null
    };
  },
  computed: {
    revokeDetails() {
      if (!this.revokeCandidate) {
        return [];
      }
      const rows = [];
      rows.push({ label: '房間號碼', value: this.revokeCandidate.roomNb || '－' });
      if (this.originalMeta?.t === 'hlrc') {
        rows.push({ label: '房間型態', value: this.revokeCandidate.roomType || '－' });
        rows.push({ label: '備註', value: this.revokeCandidate.roomMemo || '－' });
      } else if (this.originalMeta?.t === 'hlbft') {
        rows.push({ label: '票券類別', value: this.revokeCandidate.ticketType || '－' });
        rows.push({ label: '餐廳位置', value: this.revokeCandidate.location || '－' });
      }
      rows.push({ label: '憑證 CID', value: this.revokeCandidate.cid || '尚未取得' });
      return rows;
    }
  },
  methods: {
    async handleDecode() {
      if (!this.form.encryptedData.trim()) {
        this.error = '請先貼上加密字串';
        this.result = null;
        return;
      }

      this.loading = true;
      this.error = null;
      this.result = null;
      this.revokeCandidate = null;
      this.revokeMessage = null;

      this.originalMeta = this.parseOriginalMeta(this.form.encryptedData);
      const payload = {
        encryptedData: this.form.encryptedData.trim()
      };

      try {
        const response = await decodeQrCode(payload);
        this.result = response.data;
        if (!this.result.success) {
          this.error = this.result.message;
        }
        this.revokeCandidate = this.result.success ? this.extractRevokeCandidate(this.originalMeta, this.result.data) : null;
        this.revokeTitle = this.buildRevokeTitle(this.originalMeta);
        if (this.revokeCandidate) {
          await this.fetchRevokeCid();
        } else if (this.originalMeta && this.originalMeta?.t === 'hlbft') {
          this.revokeMessage = '未找到符合條件的早餐券發卡紀錄或尚未取得 CID。';
        } else if (this.originalMeta && this.originalMeta?.t === 'hlrc') {
          this.revokeMessage = '未找到符合條件的房客卡發卡紀錄或尚未取得 CID。';
        }
      } catch (err) {
        this.error = err.message || '解析失敗，請稍後再試';
        this.revokeCandidate = null;
      } finally {
        this.loading = false;
      }
    },
    handleReset() {
      this.form.encryptedData = '';
      this.result = null;
      this.error = null;
      this.originalMeta = null;
      this.revokeCandidate = null;
      this.revokeMessage = null;
      this.revokeTitle = '';
    },
    prettyJson(raw) {
      try {
        const parsed = JSON.parse(raw);
        return JSON.stringify(parsed, null, 2);
      } catch (e) {
        return raw;
      }
    },
    formatValue(value) {
      if (value === null || value === undefined) {
        return '－';
      }
      if (typeof value === 'object') {
        try {
          return JSON.stringify(value);
        } catch (e) {
          return String(value);
        }
      }
      return String(value);
    },
    parseOriginalMeta(raw) {
      try {
        const parsed = JSON.parse(raw);
        if (parsed && typeof parsed === 'object') {
          return parsed;
        }
      } catch (error) {
        return null;
      }
      return null;
    },
    buildRevokeTitle(meta) {
      if (!meta || !meta.t) {
        return '';
      }
      if (meta.t === 'hlbft') {
        return '偵測到早餐券';
      }
      if (meta.t === 'hlrc') {
        return '偵測到房客卡';
      }
      return '';
    },
    extractRevokeCandidate(meta, data) {
      if (!meta || !meta.t) {
        return null;
      }

      const candidate = {
        vcUid: '',
        roomNb: '',
        roomType: '',
        roomMemo: '',
        ticketType: '',
        location: '',
        cid: null
      };

      if (meta.t === 'hlbft') {
        candidate.vcUid = '2-16-886-1-101-90001-20004-30004-30004-40004_hlbft1023';
      } else if (meta.t === 'hlrc') {
        candidate.vcUid = '2-16-886-1-101-90001-20004-30004-30004-40004_hlrc1023';
      } else {
        return null;
      }

      const subject = this.normalizeSubject(data?.credentialSubject || data);
      candidate.roomNb = subject.room_nb || subject.roomNb || '';
      candidate.roomType = subject.room_type || subject.roomType || '';
      candidate.roomMemo = subject.room_memo || subject.roomMemo || '';
      candidate.ticketType = subject.ticket_type || subject.ticketType || '';
      candidate.location = subject.location || subject.Location || '';

      if (!candidate.roomNb) {
        return null;
      }
      return candidate;
    },
    normalizeSubject(subject) {
      if (!subject) {
        return {};
      }
      if (Array.isArray(subject)) {
        return subject.reduce((acc, item) => {
          if (item && typeof item === 'object') {
            return Object.assign(acc, item);
          }
          return acc;
        }, {});
      }
      if (typeof subject === 'object') {
        return subject;
      }
      return {};
    },
    async fetchRevokeCid() {
      if (!this.revokeCandidate) {
        return;
      }
      try {
        const response = await lookupCredentialForRevoke(this.revokeCandidate);
        if (response && response.found && response.cid) {
          this.revokeCandidate.cid = response.cid;
          this.revokeMessage = `已找到 CID：${response.cid}`;
        } else {
          this.revokeCandidate.cid = null;
          this.revokeMessage = response?.message || '未找到符合條件的發卡紀錄或尚未取得 CID。';
        }
      } catch (error) {
        this.revokeCandidate.cid = null;
        this.revokeMessage = error.message || '查詢 CID 失敗，請稍後再試。';
      }
    },
    resolveVcUid(vcRoot, fallbackType) {
      if (!vcRoot) {
        return fallbackType;
      }
      if (typeof vcRoot.credentialType === 'string') {
        return vcRoot.credentialType;
      }
      if (vcRoot.credentialType && Array.isArray(vcRoot.credentialType)) {
        const candidate = vcRoot.credentialType.find((item) => typeof item === 'string' && item.includes('hlbft'));
        if (candidate) {
          return candidate;
        }
      }
      if (typeof vcRoot.vcUid === 'string') {
        return vcRoot.vcUid;
      }
      return fallbackType;
    },
    async handleRevoke() {
      if (!this.revokeCandidate) {
        return;
      }
      if (this.originalMeta?.t === 'hlrc') {
        this.revokeMessage = '歡迎使用，祝您入住愉快。';
        window.alert('歡迎使用，祝您入住愉快。');
        return;
      }
      if (!this.revokeCandidate.cid) {
        this.revokeMessage = '尚未查得 CID，無法撤銷。';
        return;
      }
      this.revoking = true;
      this.revokeMessage = null;
      try {
        await revokeCredential(this.revokeCandidate);
        this.revokeMessage = '早餐券已成功撤銷。';
        window.alert('早餐券已成功撤銷。');
        this.revokeCandidate = null;
      } catch (error) {
        this.revokeMessage = error.message || '撤銷失敗，請稍後再試。';
      } finally {
        this.revoking = false;
      }
    }
  }
};
</script>

<style scoped>
.key-grid {
  display: grid;
  gap: 16px;
}

.key-grid .form-field {
  margin: 0;
}

textarea {
  min-height: 120px;
}

.encrypted-input {
  /* 設定輸入框高度 */
  height: 220px;
  /* 設定輸入框寬度為 100% 填滿容器 */
  width: 100%;
  /* 禁止調整大小 */
  resize: none;
  /* 設定等寬字體，方便檢視加密資料 */
  font-family: 'SFMono-Regular', Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
  /* 設定字體大小 */
  font-size: 14px;
  /* 設定行高 */
  line-height: 1.5;
  /* 設定 box-sizing 確保 padding 不會撐破寬度 */
  box-sizing: border-box;
}

.revoke-section {
  margin-top: 16px;
  padding: 16px;
  border-radius: 12px;
  background: rgba(248, 113, 113, 0.08);
  border: 1px solid rgba(248, 113, 113, 0.3);
}

.revoke-section h4 {
  margin: 0 0 8px;
  font-size: 15px;
  color: #9f1239;
}

.revoke-section ul {
  list-style: none;
  padding: 0;
  margin: 0 0 12px;
}

.revoke-section li {
  font-size: 14px;
  color: #7f1d1d;
}

.revoke-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.revoke-message {
  margin-top: 8px;
  font-size: 13px;
  color: #9f1239;
}

.button--danger {
  background: #dc2626;
  border-color: #dc2626;
  color: #ffffff;
}

.button--danger:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.result-block {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.result-status {
  font-weight: 700;
  padding: 12px 16px;
  border-radius: 12px;
  width: fit-content;
}

.result-status.success {
  color: #0f5132;
  background: #d1e7dd;
  border: 1px solid #badbcc;
}

.result-status.error {
  color: #842029;
  background: #f8d7da;
  border: 1px solid #f5c2c7;
}

.result-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
}

.result-item {
  padding: 12px;
  border: 1px solid rgba(148, 163, 184, 0.24);
  border-radius: 12px;
  background: rgba(148, 163, 184, 0.08);
}

.result-key {
  font-size: 13px;
  color: rgba(15, 23, 42, 0.65);
  display: block;
  margin-bottom: 4px;
}

.result-value {
  font-weight: 600;
  word-break: break-all;
}

.result-json pre {
  padding: 16px;
  background: #0f172a;
  color: #f8fafc;
  border-radius: 12px;
  overflow: auto;
}

.error-message {
  color: #842029;
  background: #f8d7da;
  border: 1px solid #f5c2c7;
  padding: 16px;
  border-radius: 12px;
}

.text-success {
  color: #0f5132;
}

.text-error {
  color: #842029;
}
</style>

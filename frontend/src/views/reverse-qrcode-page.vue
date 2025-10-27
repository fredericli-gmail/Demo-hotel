<template>
  <div class="container">
    <section class="card">
      <header class="section-heading">
        <div>
          <h2 class="section-title">QR Code 解析工具</h2>
          <p class="section-subtitle">貼上加密 QR Code 字串，即可驗證並還原原始資料。</p>
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

export default {
  name: 'ReverseQrcodePage',
  data() {
    return {
      form: {
        encryptedData: ''
      },
      loading: false,
      result: null,
      error: null
    };
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

      const payload = {
        encryptedData: this.form.encryptedData.trim()
      };

      try {
        const response = await decodeQrCode(payload);
        this.result = response.data;
        if (!this.result.success) {
          this.error = this.result.message;
        }
      } catch (err) {
        this.error = err.message || '解析失敗，請稍後再試';
      } finally {
        this.loading = false;
      }
    },
    handleReset() {
      this.form.encryptedData = '';
      this.result = null;
      this.error = null;
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
  height: 220px;
  resize: none;
  font-family: 'SFMono-Regular', Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
  font-size: 14px;
  line-height: 1.5;
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

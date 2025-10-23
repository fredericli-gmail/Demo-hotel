<template>
  <!-- 繁體中文註解：提供驗證端的工具介面，協助產生 QR Code 與查詢結果。 -->
  <div class="container">
    <section class="card">
      <h2>產生驗證用 QR Code</h2>
      <form @submit.prevent="handleCreateQrcode">
        <div class="form-row">
          <label for="ref">驗證服務代碼 (ref)</label>
          <input id="ref" v-model="verification.ref" required />
        </div>
        <div class="form-row">
          <label for="transaction">交易序號</label>
          <input id="transaction" v-model="verification.transactionId" required />
        </div>
        <div class="form-row checkbox-row">
          <label>
            <input type="checkbox" v-model="verification.enableCallback" />
            啟用 callback
          </label>
        </div>
        <button class="button" type="submit" :disabled="loading">送出</button>
      </form>
      <div v-if="verificationResult" class="result-block">
        <p><strong>交易序號：</strong>{{ verificationResult.transactionId }}</p>
        <p><strong>Deep Link：</strong>{{ verificationResult.authUri }}</p>
        <div v-if="verificationResult.qrcodeImage" class="qr-wrapper">
          <img :src="verificationResult.qrcodeImage" alt="驗證用 QR Code" />
        </div>
      </div>
    </section>

    <section class="card">
      <h2>查詢驗證結果</h2>
      <form @submit.prevent="handleQueryResult">
        <div class="form-row">
          <label for="queryTx">交易序號</label>
          <input id="queryTx" v-model="resultQuery.transactionId" required />
        </div>
        <button class="button" type="submit" :disabled="loading">查詢</button>
      </form>
      <div v-if="resultDetail" class="result-block">
        <p><strong>驗證結果：</strong>{{ resultDetail.verifyResult ? '成功' : '失敗' }}</p>
        <p><strong>說明：</strong>{{ resultDetail.resultDescription }}</p>
        <div v-for="item in resultDetail.data" :key="item.credentialType" class="credential-block">
          <h3>{{ item.credentialType }}</h3>
          <ul>
            <li v-for="claim in item.claims" :key="claim.ename">
              {{ claim.cname }}：{{ claim.value }}
            </li>
          </ul>
        </div>
      </div>
    </section>

    <section class="card">
      <h2>取得 Deep Link</h2>
      <form @submit.prevent="handleDeepLink">
        <div class="form-row">
          <label for="vpUid">VP 模板代碼</label>
          <input id="vpUid" v-model="deepLink.vpUid" required />
        </div>
        <button class="button" type="submit" :disabled="loading">取得</button>
      </form>
      <div v-if="deepLinkResult" class="result-block">
        <p><strong>Deep Link：</strong>{{ deepLinkResult.data?.deepLink }}</p>
      </div>
    </section>
  </div>
</template>

<script>
import { createVerificationQrcode, queryVerificationResult, requestVerifierDeepLink } from '../services/verifierService';

export default {
  name: 'VerificationPage',
  data() {
    return {
      verification: {
        ref: '',
        transactionId: '',
        enableCallback: false
      },
      resultQuery: {
        transactionId: ''
      },
      deepLink: {
        vpUid: ''
      },
      loading: false,
      verificationResult: null,
      resultDetail: null,
      deepLinkResult: null
    };
  },
  methods: {
    async handleCreateQrcode() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        this.verificationResult = await createVerificationQrcode(
          this.verification.ref,
          this.verification.transactionId,
          this.verification.enableCallback
        );
      } catch (error) {
        window.alert(error.message);
      } finally {
        this.loading = false;
      }
    },
    async handleQueryResult() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        this.resultDetail = await queryVerificationResult(this.resultQuery.transactionId);
      } catch (error) {
        window.alert(error.message);
      } finally {
        this.loading = false;
      }
    },
    async handleDeepLink() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        this.deepLinkResult = await requestVerifierDeepLink(this.deepLink.vpUid);
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
.checkbox-row {
  margin: 12px 0;
}

.qr-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

.qr-wrapper img {
  width: 200px;
  height: 200px;
}

.credential-block {
  margin-top: 16px;
  background-color: #f1f5f9;
  border-radius: 8px;
  padding: 16px;
}

.credential-block ul {
  padding-left: 20px;
}
</style>

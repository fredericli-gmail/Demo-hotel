<template>
  <!-- 繁體中文註解：提供停用、復用、撤銷等狀態管理功能。 -->
  <div class="container">
    <section class="card">
      <h2>單筆卡片狀態調整</h2>
      <form @submit.prevent="handleSingleAction">
        <div class="form-row">
          <label for="singleCid">CID</label>
          <input id="singleCid" v-model="single.cid" required />
        </div>
        <div class="form-row">
          <label for="singleAction">操作類型</label>
          <select id="singleAction" v-model="single.action" required>
            <option value="revocation">撤銷</option>
            <option value="suspension">停用</option>
            <option value="recovery">復用</option>
          </select>
        </div>
        <button class="button" type="submit" :disabled="loading">送出</button>
      </form>
      <div v-if="singleResult" class="result-block">
        <p><strong>最新狀態：</strong>{{ singleResult.credentialStatus }}</p>
      </div>
    </section>

    <section class="card">
      <h2>批次卡片狀態調整</h2>
      <form @submit.prevent="handleBatchAction">
        <div class="form-row">
          <label for="batchAction">操作類型</label>
          <select id="batchAction" v-model="batch.action" required>
            <option value="revocation">撤銷</option>
            <option value="suspension">停用</option>
            <option value="recovery">復用</option>
          </select>
        </div>
        <div class="form-row">
          <label for="cids">CID 清單 (以逗號分隔)</label>
          <textarea id="cids" v-model="batch.cidText" rows="4" required></textarea>
        </div>
        <button class="button" type="submit" :disabled="loading">送出</button>
      </form>
      <div v-if="batchResult" class="result-block">
        <p><strong>操作：</strong>{{ batchResult.action }}</p>
        <p><strong>成功清單：</strong>{{ batchResult.success.join(', ') }}</p>
        <div v-if="batchResult.fail && batchResult.fail.length">
          <h3>失敗明細</h3>
          <ul>
            <li v-for="item in batchResult.fail" :key="item.code">
              {{ item.code }} - {{ item.message }}：{{ item.cids.join(', ') }}
            </li>
          </ul>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { changeStatus, changeStatusBatch } from '../services/credentialService';

export default {
  name: 'LifecyclePage',
  data() {
    return {
      single: {
        cid: '',
        action: 'revocation'
      },
      batch: {
        action: 'revocation',
        cidText: ''
      },
      loading: false,
      singleResult: null,
      batchResult: null
    };
  },
  methods: {
    async handleSingleAction() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        this.singleResult = await changeStatus(this.single.cid, this.single.action);
      } catch (error) {
        window.alert(error.message);
      } finally {
        this.loading = false;
      }
    },
    async handleBatchAction() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        const payload = {
          action: this.batch.action,
          cids: this.batch.cidText.split(',').map((item) => item.trim()).filter((item) => item.length > 0)
        };
        this.batchResult = await changeStatusBatch(payload);
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
textarea {
  width: 100%;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #cbd5e1;
}

.result-block {
  margin-top: 16px;
  line-height: 1.6;
}

ul {
  padding-left: 20px;
}
</style>

<template>
  <!-- 繁體中文註解：提供多種查詢方式以掌握房卡發行狀態。 -->
  <div class="container">
    <section class="card">
      <h2>依交易序號查詢</h2>
      <form @submit.prevent="handleTransactionLookup">
        <div class="form-row">
          <label for="transactionId">交易序號</label>
          <input id="transactionId" v-model="transactionId" required />
        </div>
        <button class="button" type="submit" :disabled="loading">查詢</button>
      </form>
      <div v-if="transactionResult" class="result-block">
        <p><strong>Credential JWT：</strong></p>
        <textarea readonly rows="6">{{ transactionResult.credential }}</textarea>
      </div>
    </section>

    <section class="card">
      <h2>依資料標籤查詢</h2>
      <form @submit.prevent="handleDataTagLookup">
        <div class="form-row">
          <label for="dataTag">資料標籤</label>
          <input id="dataTag" v-model="dataTag" required />
        </div>
        <div class="form-row">
          <label>分頁設定</label>
          <div class="pagination">
            <input type="number" v-model.number="page" placeholder="頁碼" />
            <input type="number" v-model.number="size" placeholder="每頁筆數" />
          </div>
        </div>
        <button class="button" type="submit" :disabled="loading">查詢</button>
      </form>
      <div v-if="dataTagResult" class="result-block">
        <p><strong>標籤：</strong>{{ dataTagResult.dataTag }}</p>
        <table>
          <thead>
            <tr>
              <th>CID</th>
              <th>狀態</th>
              <th>發行時間</th>
              <th>到期時間</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in dataTagResult.vcList" :key="item.cid">
              <td>{{ item.cid }}</td>
              <td>{{ item.credentialStatus }}</td>
              <td>{{ item.issuranceDate }}</td>
              <td>{{ item.expirationDate }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<script>
import { queryByTransaction, queryByDataTag } from '../services/credentialService';

export default {
  name: 'InquiryPage',
  data() {
    return {
      transactionId: '',
      dataTag: '',
      page: null,
      size: null,
      loading: false,
      transactionResult: null,
      dataTagResult: null
    };
  },
  methods: {
    async handleTransactionLookup() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        this.transactionResult = await queryByTransaction(this.transactionId);
      } catch (error) {
        window.alert(error.message);
      } finally {
        this.loading = false;
      }
    },
    async handleDataTagLookup() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      try {
        this.dataTagResult = await queryByDataTag(this.dataTag, this.page, this.size);
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
.result-block {
  margin-top: 16px;
}

textarea {
  width: 100%;
  padding: 12px;
  border-radius: 6px;
  border: 1px solid #cbd5e1;
}

.pagination {
  display: flex;
  gap: 12px;
}

.pagination input {
  width: 120px;
  padding: 8px;
  border-radius: 6px;
  border: 1px solid #cbd5e1;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 12px;
}

th,
td {
  border: 1px solid #e2e8f0;
  padding: 8px;
  text-align: left;
}
</style>

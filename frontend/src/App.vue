<template>
  <!-- 繁體中文註解：主畫面架構，包含導覽列與內容區。 -->
  <div class="layout">
    <header class="layout__header">
      <h1>飯店電子房卡控台</h1>
      <nav class="layout__nav">
        <RouterLink to="/issue">發卡作業</RouterLink>
        <RouterLink to="/inquiry">發卡查詢</RouterLink>
        <RouterLink to="/lifecycle">卡片管理</RouterLink>
        <RouterLink to="/verification">驗證工具</RouterLink>
      </nav>
    </header>
    <main class="layout__content">
      <RouterView />
    </main>
  </div>
</template>

<script>
// 繁體中文註解：App 組件僅負責包裝頁面結構，不需額外邏輯。
export default {
  name: 'AppContainer'
};
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout__header {
  background-color: #0f766e;
  color: #ffffff;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.layout__nav {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.layout__nav a {
  color: #ffffff;
  font-weight: 600;
}

.layout__content {
  flex: 1;
  padding: 32px 16px;
}
</style>

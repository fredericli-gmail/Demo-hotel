<template>
  <!-- 繁體中文註解：主畫面架構，包含導覽列與內容區。 -->
  <div class="layout">
    <header class="layout__header">
      <div class="layout__header-inner">
        <div class="layout__brand">
          <h1>飯店電子房卡控台</h1>
          <p>極速發行 · 安全驗證 · 一站管理旅客憑證</p>
        </div>
        <nav class="layout__nav">
          <RouterLink to="/issue">發卡作業</RouterLink>
          <RouterLink to="/tickets/breakfast">早餐券發放</RouterLink>
          <!-- <RouterLink to="/inquiry">發卡查詢</RouterLink>
          <RouterLink to="/lifecycle">卡片管理</RouterLink>
          <RouterLink to="/verification">驗證工具</RouterLink> -->
          <RouterLink to="/reverse-qrcode">驗證房卡/票券</RouterLink>
        </nav>
      </div>
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
  position: sticky;
  top: 0;
  z-index: 10;
  background: rgba(255, 255, 255, 0.85);
  border-bottom: 1px solid rgba(148, 163, 184, 0.18);
  backdrop-filter: blur(18px);
}

.layout__header-inner {
  width: min(1080px, 92%);
  margin: 0 auto;
  padding: 28px 0;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.layout__brand h1 {
  font-size: 26px;
  margin: 0;
  color: #0f172a;
  font-weight: 700;
}

.layout__brand p {
  margin: 6px 0 0;
  font-size: 14px;
  color: var(--color-text-muted);
  letter-spacing: 0.04em;
}

.layout__nav {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  padding: 6px;
  border-radius: 999px;
  background: rgba(15, 23, 42, 0.06);
}

.layout__nav a {
  padding: 10px 18px;
  border-radius: 999px;
  font-size: 14px;
  font-weight: 600;
  color: rgba(15, 23, 42, 0.7);
  transition: background 0.2s ease, color 0.2s ease, transform 0.2s ease;
}

.layout__nav a:hover {
  transform: translateY(-1px);
  color: rgba(15, 23, 42, 0.95);
}

.layout__nav a.router-link-active {
  background: linear-gradient(135deg, var(--color-primary), #5cadff);
  color: #ffffff;
  box-shadow: 0 10px 20px rgba(10, 132, 255, 0.18);
}

.layout__content {
  flex: 1;
  padding: 48px 0 64px;
}
</style>

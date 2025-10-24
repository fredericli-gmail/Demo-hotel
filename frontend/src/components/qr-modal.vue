<template>
  <!-- 繁體中文註解：半透明背景加上內容卡片，集中展示 QR Code 與交易資訊。 -->
  <transition name="modal-fade">
    <div v-if="visible" class="modal-overlay" @click.self="$emit('close')">
      <div class="modal-card">
        <button class="modal-close" type="button" @click="$emit('close')">×</button>
        <header class="modal-header">
          <h3>{{ title }}</h3>
          <p v-if="description" class="modal-description">{{ description }}</p>
        </header>
        <section v-if="result" class="modal-body">
          <div class="modal-grid">
            <div class="modal-info">
              <p class="modal-label">交易序號</p>
              <p class="modal-value">{{ result.transactionId }}</p>
              <div v-if="result.deepLink" class="modal-link">
                <p class="modal-label">Deep Link</p>
                <a :href="result.deepLink" target="_blank" rel="noopener">
                  {{ result.deepLink }}
                </a>
              </div>
            </div>
            <div v-if="result.qrCode" class="modal-qr">
              <img :src="result.qrCode" alt="VC QR Code" />
              <span>請以旅客行動裝置掃描</span>
            </div>
          </div>
        </section>
        <footer class="modal-footer">
          <button class="button" type="button" @click="$emit('close')">完成</button>
        </footer>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'QrModal',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '操作完成'
    },
    description: {
      type: String,
      default: ''
    },
    result: {
      type: Object,
      default() {
        return null;
      }
    }
  }
};
</script>

<style scoped>
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.25s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: rgba(15, 23, 42, 0.35);
  backdrop-filter: blur(10px);
  z-index: 30;
}

.modal-card {
  width: min(780px, 90%);
  background: rgba(255, 255, 255, 0.94);
  border-radius: 24px;
  box-shadow: 0 24px 48px rgba(15, 23, 42, 0.22);
  border: 1px solid rgba(148, 163, 184, 0.2);
  position: relative;
  padding: 36px 40px 32px;
}

.modal-close {
  position: absolute;
  top: 18px;
  right: 22px;
  border: none;
  background: rgba(15, 23, 42, 0.06);
  border-radius: 999px;
  width: 34px;
  height: 34px;
  font-size: 18px;
  line-height: 1;
  cursor: pointer;
  transition: background 0.2s ease, transform 0.2s ease;
}

.modal-close:hover {
  background: rgba(15, 23, 42, 0.12);
  transform: scale(1.05);
}

.modal-header h3 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #0f172a;
}

.modal-description {
  margin-top: 8px;
  font-size: 14px;
  color: rgba(15, 23, 42, 0.65);
}

.modal-body {
  margin-top: 28px;
}

.modal-grid {
  display: grid;
  gap: 24px;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  align-items: center;
}

.modal-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.modal-label {
  margin: 0;
  font-size: 13px;
  color: rgba(15, 23, 42, 0.55);
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.modal-value {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #0f172a;
  word-break: break-all;
}

.modal-link a {
  display: inline-block;
  margin-top: 4px;
  font-size: 14px;
  color: var(--color-primary);
  word-break: break-all;
}

.modal-qr {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.modal-qr img {
  width: 220px;
  height: 220px;
  border-radius: 20px;
  border: 1px solid rgba(15, 23, 42, 0.08);
  box-shadow: 0 14px 32px rgba(15, 23, 42, 0.18);
}

.modal-qr span {
  font-size: 13px;
  color: rgba(15, 23, 42, 0.55);
}

.modal-footer {
  margin-top: 32px;
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 640px) {
  .modal-card {
    padding: 28px 24px 24px;
  }

  .modal-qr img {
    width: 180px;
    height: 180px;
  }
}
</style>

import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// 繁體中文註解：Vite 設定檔，啟用 Vue 插件並預留代理設定空間。
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: process.env.VITE_BACKEND_ORIGIN || 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
});

import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// 繁體中文註解：統一設定前端部署在 /demohotel/ 路徑下。
const contextRoot = '/demohotel/';
const apiProxyPath = `${contextRoot}api`;

// 繁體中文註解：Vite 設定檔，啟用 Vue 插件並預留代理設定空間。
export default defineConfig({
  base: contextRoot,
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      [apiProxyPath]: {
        target: process.env.VITE_BACKEND_ORIGIN || 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
});

// 繁體中文註解：建立 axios 實例並統一設定 Access Token 與錯誤處理。
import axios from 'axios';

const baseURL = `${import.meta.env.BASE_URL}api`;

const httpClient = axios.create({
  baseURL,
  timeout: 15000
});

httpClient.interceptors.request.use((config) => {
  // 繁體中文註解：預留從 localStorage 讀取 Token 的流程，現階段先使用後端設定的 Access Token。
  return config;
});

httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error.response && error.response.data ? error.response.data : '系統發生未預期錯誤';
    return Promise.reject(new Error(message));
  }
);

export default httpClient;

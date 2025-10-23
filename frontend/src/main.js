// 繁體中文註解：系統進入點，負責建立 Vue 應用與注入路由。
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import './styles/base.scss';

const app = createApp(App);
app.use(router);
app.mount('#app');

// 繁體中文註解：定義前端路由，將各功能頁掛載至指定路徑。
import { createRouter, createWebHistory } from 'vue-router';
import IssuePage from '../views/IssuePage.vue';
import InquiryPage from '../views/InquiryPage.vue';
import LifecyclePage from '../views/LifecyclePage.vue';
import VerificationPage from '../views/VerificationPage.vue';
import ReverseQrcodePage from '../views/reverse-qrcode-page.vue';
import BreakfastTicketPage from '../views/breakfast-ticket-page.vue';

const routes = [
  {
    path: '/',
    redirect: '/issue'
  },
  {
    path: '/issue',
    component: IssuePage
  },
  {
    path: '/inquiry',
    component: InquiryPage
  },
  {
    path: '/lifecycle',
    component: LifecyclePage
  },
  {
    path: '/verification',
    component: VerificationPage
  },
  {
    path: '/reverse-qrcode',
    component: ReverseQrcodePage
  },
  {
    path: '/tickets/breakfast',
    component: BreakfastTicketPage
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;

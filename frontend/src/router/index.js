// 繁體中文註解：定義前端路由，將各功能頁掛載至指定路徑。
import { createRouter, createWebHistory } from 'vue-router';
import IssuePage from '../views/IssuePage.vue';
import InquiryPage from '../views/InquiryPage.vue';
import LifecyclePage from '../views/LifecyclePage.vue';
import VerificationPage from '../views/VerificationPage.vue';

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
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;

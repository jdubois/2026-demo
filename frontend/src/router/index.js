import { createRouter, createWebHashHistory } from 'vue-router'
import TicketBoardView from '../views/TicketBoardView.vue'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'tickets',
      component: TicketBoardView,
    },
  ],
})

export default router

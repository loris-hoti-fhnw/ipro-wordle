import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router';
import HomeView from '../views/HomeView.vue'
import GameView from '../views/GameView.vue'

const routes: Array<RouteRecordRaw> = [
  { path: '/', component: HomeView },
  { path: '/game/:id', component: GameView, props: true }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

import { createRouter, createWebHistory } from 'vue-router'

const routes = [
]

const router = createRouter({
  history: createWebHistory(), //将createWebHashHistory改为createWebHistory去掉路径中的#
  routes
})

export default router

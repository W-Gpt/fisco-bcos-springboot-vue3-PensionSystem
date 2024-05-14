import { createRouter, createWebHistory } from 'vue-router'
import login from '../views/login.vue'
// import reister from '../views/register.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: login
    },
    {
      path:'/register',
      name:'register',
      component: () => import('../views/register.vue')
    },
    {path:'/gongan',
      name:'gongan',
      component: () => import('../views/gongAn.vue')
    }
    // {
    //   path: '/',
    //   name: 'home',
    //   component: HomeView
    // },
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue')
    // }
  ]
})

export default router

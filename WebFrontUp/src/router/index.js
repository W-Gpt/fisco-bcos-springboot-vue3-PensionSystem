import { createRouter, createWebHistory } from 'vue-router'
import login from '../views/login/login.vue'
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
      component: () => import('../views/login/register.vue')
    },
    {path:'/gongan',
      name:'gongan',
      component: () => import('../views/gongAn.vue')
    },
    {
      path:'/SocialSDeptHome',
      name:'SocialSDeptHome',
      component:()=>import('../views/SocialSecDept/SocialSecDeptHome.vue'),
      children:[
        {
          path:'/allCompany',
          name:'allCompany',
          component:()=>import('../views/SocialSecDept/allCompany.vue'),
          meta:{
            title:'公司基本信息',
          }
        },
        {
          path:'/payMentC',
          name:'payMentC',
          component:()=>import('../views/SocialSecDept/PayMentC.vue'),
          meta:{
            title:'缴纳公司',
          }
        },
        {
          path:'/payMentPer',
          name:'payMentPer',
          component:()=>import('../views/SocialSecDept/PayMentPer.vue'),
          meta:{
            title:'缴纳个人'
          }
        },
        {
          path:'/transferReview',
          name:'transferReview',
          component:()=>import('../views/SocialSecDept/transferReview.vue'),
          meta:{
            title:'账户转移审核'
          }
        }
      ]
    },{
      path:'/laborHome',
      name:'laborHome',
      component:()=>import('../views/labor/laborHome.vue'),
      children:[
        {
          path:'/personInfo',
          name:'personInfo',
          component:()=>import('../views/labor/laborHome.vue'),
        }
      ]
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

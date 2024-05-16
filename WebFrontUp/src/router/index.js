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
      redirect:'/allCompany',
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
      redirect:'/companyInfo',
      component:()=>import('../views/labor/laborHome.vue'),
      children:[
        {
          path:'/companyInfo',
          name:'companyInfo',
          component:()=>import('../views/labor/companyInfo.vue'),
          meta:{
            title:'公司列表'
          }
        },
        {
          path:'/laborInfo',
          name:'laborInfo',
          component:()=>import('../views/labor/laborInfo.vue'),
          meta:{
            title:'工作信息列表'
          }
        }
      ]
    },{
      path:'/companyHome',
      name:'companyHome',
      redirect:'/insuredPerson',
      component:()=>import('../views/company/companyHome.vue'),
      children:[
        {
          path:"/insuredPerson",
          name:'insuredPerson',
          component:()=>import('../views/company/insuredPerson.vue'),
          meta:{
            title:'参保人员'
          }
        },{
          path:"/uninsuredPer",
          name:'uninsuredPer',
          component:()=>import('../views/company/unInsuredPer.vue'),
          meta:{
            title:'未参保人员'
          }
        },{
          path:"/leavePer",
          name:'leavePer',
          component:()=>import('../views/company/leavePer.vue'),
          meta:{
            title:'离职人员信息'
          }
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

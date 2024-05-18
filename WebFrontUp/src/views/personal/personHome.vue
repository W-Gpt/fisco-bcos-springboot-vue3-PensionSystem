<template >
    <div class="common-layout" >
      <el-container >
        <el-header><HeaderV /></el-header>
        <el-container >
          <el-aside width="200px" >
            <el-menu router class="el-menu-demo" default-active="companyInfo" style="height:calc(100vh - 60px);">
              <el-menu-item v-for="item in this.tabList" :router="item.path" style="justify-content: center" :index="item.name">
                 {{ item.meta.title }}
              </el-menu-item>
            </el-menu>
          </el-aside>
          <el-main style="overflow: hidden;">
            <router-view></router-view>
          </el-main>
        </el-container>
      </el-container>
    </div>
  </template>
  <script>
  // import { ref } from 'vue';
  import HeaderV from '../../components/HeaderV.vue'
  import router from '../../router/index.js'
  import request from '@/utils/request.js';
  export default {
    components: { HeaderV },
    data(){
      return{
       
        tabList:router.options.routes[6].children
      }
    },
    mounted(){
     
    },
    methods:{
    }
  }
  </script>
  <style scoped>
  header{
    padding: 0;
  }
  
  
  </style>
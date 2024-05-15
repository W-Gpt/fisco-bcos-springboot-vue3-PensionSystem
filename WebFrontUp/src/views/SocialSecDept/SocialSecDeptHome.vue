<template>
    <div>
        <el-container>
            <el-header><HeaderV></HeaderV></el-header>
            <el-container>
                <el-aside>
                    <el-menu  router  default-active="allCompany" class="el-menu-vertical-demo">
                        <el-menu-item v-for="item in this.tabList" :router="item.path" style="justify-content: center" :index="item.name">
                            {{ item.meta.title }}
                        </el-menu-item>
                    </el-menu>
                    
                </el-aside>
                <el-main>
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>
<script>
import HeaderV from '../../components/HeaderV.vue'
import router from '../../router/index.js'
export default {
    components: { HeaderV },
    data(){
        return{
            tabList:router.options.routes[3].children
        }
    },
mounted(){
    console.log(router.options.routes[3].children);
}
}
</script>
<style>
.el-header{
  padding: 0;
}

</style>
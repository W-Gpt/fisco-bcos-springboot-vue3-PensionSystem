<template>
    <el-header class="top-box">
      <span v-show="this.userType=='1'|| this.userType=='5'" style="position: absolute;left: 0;margin-left: 12px;">账户总额:{{  }}</span>
      <div style="margin-right: 20px;">
        <img class="logo" src="../assets/logo.jfif">
      </div>
      <span><h2 style="color:black;">区块链养老保险系统平台</h2></span>
      <span style="position: absolute;right: 0;margin-right: 10px;">当前身份：{{this.username}} <el-button @click="toBack">注销</el-button></span>
    </el-header>
</template>
<script>
export default {
    data(){
      return{
        username: "公安总局",
        userType:null
      }
    },
    mounted(){
      this.load();
    },
    methods:{
      toBack(){
        localStorage.removeItem('username');
        localStorage.removeItem('userAddress');
        localStorage.removeItem('userType');
        this.$router.push('/');
      },
      load(){
        if(localStorage.getItem('username')!=null){
          this.username=localStorage.getItem('username');
        }
        this.userType=localStorage.getItem('userType');
      }
      
    }
}
</script>
<style  scoped>
header{
  padding: 0;
}
.top-box{
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgb(178,220,232);
  padding: 5px 10px 0 10px;
}
.logo{
  width: 50px;
  display: block;
  margin-left: 20px;
  }
</style>
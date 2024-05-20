<template>
  <div class="login-center">
    <div class="login-container">
      <h2 style="text-align: center;">Forya养老保险系统</h2>
      <el-form :model="this.loginForm" class="login-form"  label-width="auto">
        <el-form-item label="用户类型" prop="userType" >
            <el-select v-model="loginForm.userType" placeholder="选择用户">
                <el-option v-for="item in this.users" :value="item.value" :label="item.label" :key="item.value" />
            </el-select>
        </el-form-item>
        <el-form-item label="用户名" prop="username" class="login-form-item" v-if="loginForm.userType!='4'">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" class="login-form-item" v-show="loginForm.userType!='4' && this.loginForm.userType!='5'">
          <el-input v-model="this.loginForm.password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="区块链地址" v-show="loginForm.userType=='4' || loginForm.userType=='5'">
          <el-input v-model="this.loginForm.address" />
        </el-form-item>
        <!-- <el-form-item label="用户类型" prop="userType" >
            <el-select v-model="loginForm.userType" placeholder="选择用户">
                <el-option v-for="item in this.users" :value="item.value" :label="item.label" :key="item.value" />
            </el-select>
        </el-form-item> -->
        
        <el-form-item class="login-register-item">
            <el-button type="primary" class="login-register-btn" @click="login">登录</el-button>
            <el-button type="success" class="login-register-btn" @click="registerTo">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';
import request from '@/utils/request';

export default {
  data(){
    return{
      gongan:''
    }
  },
  components: {
    ElForm,
    ElFormItem,
    ElInput,
    ElButton
  },
  methods:{
    login(){
      if(this.loginForm.userType=="4"){
        console.log("nihao")
        localStorage.setItem('userAddress',this.loginForm.address);
        this.$router.push('/gongan');
        this.$message({
            type:"success",
            message:"登录成功"
            })
      }else{
        request.post('/user/login',this.loginForm).then((res)=>{
        if(res.code==200){
          this.$message({
            type:"success",
            message:res.msg
            })
           localStorage.setItem('username',this.loginForm.username);
           localStorage.setItem('userType',this.loginForm.userType);
           if(this.loginForm.userType=="3"){
            console.log("zaizhe");
              this.$router.push('/SocialSDeptHome');
           }
           if(this.loginForm.userType=="2"){
            console.log("zaizhe");
              this.$router.push('/laborHome');
           }
           if(this.loginForm.userType=="1"){
            console.log("zaizhe");
              this.$router.push('/personHome');
           }
           if(this.loginForm.userType=="5"){
            console.log("zaizhe");
            localStorage.setItem('userAddress',this.loginForm.address);
            this.$router.push('/companyHome');
           }
        }else{
          this.$message({
            type:"warning",
            message:res.msg
            })
        }
        console.log(res);
      })
      }
      
      // console.log("ea easd")
      // this.$refs.loginForm.validate((valid) => {
      //   if (valid) {
      //     alert('登录成功');
      //     // 此处可添加登录逻辑
      //   } else {
      //     return false;
      //   }
      // });
    },
    registerTo(){
      this.$router.push('/register');
    }
  },
  setup() {
    const loginForm =ref({
      username: '',
      password: ''
    });
    // const gongan="";
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    };
    return {
      loginForm,
      rules,
      // gongan,
      users:[{value:'1',
            label:'普通个人'},
            {value:'2',
            label:'劳动局'},
        {value:'3',label:'社保局'},
      {value:'4',label:'公安'},
      {value:'5',label:'公司'}]
    };
  }
};
</script>

<style>
  .login-center {
    background: url(@/assets/backup.jpg);
    background-size: cover;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }

  .login-container {
    width: 400px;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }

  .login-form {
    margin-top: 20px;
  }

  .login-register-item {
    margin-bottom: 20px;
    justify-content: center;
    align-items: center;
    display: flex 
  }

  .login-register-btn{
    width: 35%;
    margin-left:50px;
  }
</style>

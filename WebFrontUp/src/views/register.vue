<template>
    <div class="register-center">
      <div class="register-container">
        <h2 style="text-align: center;">Forya养老保险系统</h2>
        <h5 style="text-align: center;">注册</h5>
        <el-form :model="registerForm" ref="registerForm" class="register-form" :rules="rules" label-width="80px">
          <el-form-item label="用户名" prop="username" class="register-item">
            <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password" class="register-item">
            <el-input v-model="registerForm.password" placeholder="请输入密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="所在城市" prop="city" >
            <el-input  v-model="registerForm.city" placeholder="请输入城市"></el-input>
          </el-form-item>
          <el-form-item label="用户类型" prop="userType">
            <radio-group v-model="registerForm.userType">
                <el-radio :value=1>普通个人</el-radio>
                <el-radio  :value=2>劳动局</el-radio>
                <el-radio :value=3>社保局</el-radio>
            </radio-group>
            
          </el-form-item>
          <el-from-item>
             <div class="register-item">
              <el-button type="primary" class="register-btn" @click="register">登录</el-button>
              <el-button type="primary" class="register-btn" @click="registerTo">注册</el-button>
            </div>
          </el-from-item>
        </el-form>  
         
      </div>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue';
//   import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';
  import request from '@/utils/request';
  
  export default {
    data(){
      return{
        
      }
    },
    
    methods:{
      register(){
        console.log("ea easd")
        refs.registerForm.validate((valid) => {
          if (valid) {
            alert('登录成功');
            // 此处可添加登录逻辑
          } else {
            return false;
          }
        });
      },
      registerTo(){
        this.$router.push();
      }
    },
    setup() {
      const registerForm = ref({
        username: '',
        password: ''
      });
  
      const rules = {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      };
      return {
        registerForm,
        rules
      };
    }
  };
  </script>
  
  <style>
    .register-center {
      background: url(@/assets/backup.jpg);
      background-size: cover;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
  
    .register-container {
      width: 400px;
      padding: 20px;
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }
  
    .register-form {
      margin-top: 20px;
    }
  
    .register-item {
      margin-bottom: 20px;
      justify-content: center;
      align-items: center;
      display: flex 
    }
  
    .register-btn{
      width: 35%;
      margin-left:50px;
    }
  </style>
  
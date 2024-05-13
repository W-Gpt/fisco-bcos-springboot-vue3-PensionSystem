<template>
  <div class="login-center">
    <div class="login-container">
      <h2 style="text-align: center;">Forya养老保险系统</h2>
      <el-form :model="loginForm" ref="loginForm" class="login-form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username" class="login-form-item">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" class="login-form-item">
          <el-input v-model="loginForm.password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item class="login-form-item">
          <el-button type="primary" class="login-submit-btn" @click="submitForm">登录</el-button>
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
      
    }
  },
  components: {
    ElForm,
    ElFormItem,
    ElInput,
    ElButton
  },
  methods:{

  },
  setup() {
    const loginForm = ref({
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

    const submitForm = () => {
      refs.loginForm.validate((valid) => {
        if (valid) {
          alert('登录成功');
          // 此处可添加登录逻辑
        } else {
          return false;
        }
      });
    };

    return {
      loginForm,
      rules,
      submitForm
    };
  }
};
</script>

<style>
  .login-center {
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

  .login-form-item {
    margin-bottom: 20px;
  }

  .login-submit-btn {
    width: 100%;
  }
</style>

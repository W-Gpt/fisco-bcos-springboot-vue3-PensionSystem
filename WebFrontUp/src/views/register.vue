<template>
    <div class="register-center">
      <div class="register-container">
        <h2 style="text-align: center;">Forya养老保险系统</h2>
        <h5 style="text-align: center;">注册</h5>
        <el-form :model="registerForm" class="register-form" :rules="rules" label-width="auto">
          <el-form-item label="用户名" prop="username" class="register-item">
            <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password" class="register-item">
            <el-input v-model="registerForm.password" placeholder="请输入密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="身份证" prop="id" class="register-item" v-show="registerForm.userType==1">
            <el-input v-model="registerForm.id" placeholder="请输入身份证"></el-input>
          </el-form-item>
          <el-form-item label="区块链地址" prop="address" v-show="registerForm.userType==2 || registerForm.userType==3">
            <el-input  v-model="registerForm.address" placeholder="请输入区块链地址" ></el-input>
          </el-form-item>
          <el-form-item label="所在城市" prop="city" v-show="registerForm.userType==2 || registerForm.userType==3">
            <el-input  v-model="registerForm.city" placeholder="请输入城市" ></el-input>
          </el-form-item>
          <el-form-item label="最大缴费基数" prop="maxBase" v-show="registerForm.userType==3">
            <el-input  v-model="registerForm.maxBase" placeholder="请输入最大缴费基数"></el-input>
          </el-form-item>
          <el-form-item label="最小缴费基数" prop="minBase" v-show="registerForm.userType==3">
            <el-input  v-model="registerForm.minBase" placeholder="请输入最小缴费基数"></el-input>
          </el-form-item>
          <el-form-item label="个人缴费比例" prop="personalRate" v-show="registerForm.userType==3">
            <el-input  v-model="registerForm.personalRate" placeholder="请输入个人缴费比例"></el-input>
          </el-form-item>
          <el-form-item label="公司缴费比例" prop="companyRate" v-show="registerForm.userType==3">
            <el-input  v-model="registerForm.companyRate" placeholder="请输入公司缴费比例"></el-input>
          </el-form-item>
          <el-form-item label="用户类型" prop="userType">
            <el-select v-model="registerForm.userType" placeholder="选择用户">
                <el-option v-for="item in this.users" :value="item.value" :label="item.label" :key="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item class="register-item">
            
              <el-button type="primary" class="register-btn" @click="loginTo">返回</el-button>
              <el-button type="success" class="register-btn" @click="register">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue';
  // import { ElForm, ElFormItem, ElInput, ElButton,ElSelect,ElRadioGroup,ElRadio} from 'element-plus';
  import request from '@/utils/request.js';
  
  export default {
    data(){
      return{
        
      }
    },
  //   components: {
  //   ElForm,
  //   ElFormItem,
  //   ElInput,
  //   ElButton
  // },
    
    methods:{
      register(){
        request.post('/user/register',this.registerForm).then((res)=>{
          console.log(res);
          if(res.code==200){
          this.$message({
            type:"success",
            message:res.msg
            })
        }else{
          this.$message({
            type:"warning",
            message:res.msg
            })
        }
        });
        // console.log("ea easd")
        // this.$refs.registerForm.validate((valid) => {
        //   if (valid) {
        //     alert('登录成功');
        //     // 此处可添加登录逻辑
        //   } else {
        //     return false;
        //   }
        // });
        this.registerForm={};
      },
      loginTo(){
        this.$router.push('/');
      }
    },
    setup() {
      const registerForm =ref({
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
        rules,
        users:[{value:1,
            label:'普通个人'},
            {value:2,
            label:'劳动局'},
        {value:3,label:'社保局'}]
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
      width: 600px;
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
      width: 25%;
      margin-left:130px;
    }
  </style>
  
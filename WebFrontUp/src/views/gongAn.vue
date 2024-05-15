<template >
  <div class="common-layout" >
    <el-container >
      <el-header><HeaderV /></el-header>
      <el-container >
        <el-aside width="200px" >
          <el-menu class="el-menu-demo" default-active="2" style="height:calc(100vh - 60px);">
            <el-menu-item>
              <span>人员信息 </span>
            </el-menu-item>
    </el-menu>
        </el-aside>
        <el-main style="padding: 0;overflow: hidden;">
          <el-row :gutter="10" >
            <el-col :span="6" style="border-right: 1px solid #dcdfe6;border-bottom: 1px solid #dcdfe6; padding: 25px;height: 230px;" >
              <el-form v-model="this.personalInfo" label-width="auto">
                <el-form-item label="身份证号码" prop="id">
                  <el-input v-model="this.personalInfo.id" />
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="this.personalInfo.name" />
                </el-form-item>
                <el-form-item label="年龄" prop="age">
                  <el-input v-model="this.personalInfo.age" />
                </el-form-item>
              </el-form>
              <el-button type="primary" style="float: right;" @click="addPersonalInfo">录入人员信息</el-button>
            </el-col>
            <el-col :span="15" style="padding: 20px 0 0 0;">
              <el-table :data="this.PersonalList" style="width: 100%" border>
                <el-table-column  label="身份证号" prop="id" />
                <el-table-column  label="姓名" prop="name" />
                <el-table-column  label="年龄" prop="age" />
              </el-table>
            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
// import { ref } from 'vue';
import HeaderV from '../components/HeaderV.vue';
import request from '@/utils/request.js';
export default {
  components: { HeaderV },
  data(){
    return{
      PersonalList:[],
      personalInfo:{ 
        id:null,
        age:null,
        name:""
    },
    }
  },
  mounted(){
    this.getPersonalList();
  },
  methods:{
    getPersonalList(){
      request.get('/security/getAllPerson').then((res)=>{
        console.log(res);
        this.PersonalList=res.data;
        console.log(this.PersonalList[2]);
      })
    },

    addPersonalInfo(){
      request.post('/security/addPerson',this.personalInfo).then((res)=>{
        console.log(res);
        if(res.code==200){
          this.$message({
            type:"success",
            message:res.msg
            });
            this.getPersonalList();
        }else{
          this.$message({
            type:"warning",
            message:res.msg
            })
        }
      })
    }
  },
  setup(){
    // const personalInfo=ref({
    //   id:null,
    //   age:null,
    //   name:""
    // });

    return{
      // personalInfo,
    }
  }
}
</script>
<style scoped>
header{
  padding: 0;
}


</style>


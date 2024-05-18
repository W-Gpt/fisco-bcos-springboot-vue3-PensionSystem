<template>
  <el-row>
      <el-table :data="this.applicationList" style="width: 100%" border>
                <el-table-column  label="转移申请号" prop="index" />
                <el-table-column  label="身份证号" prop="id" />
                <el-table-column  label="原公司" prop="fromCompany" />
                <el-table-column  label="申请转入公司" prop="toCompany" />
                <el-table-column  label="原社保局" prop="fromSocialSecDept" />
                <el-table-column  label="转入社保局" prop="toSocialSecDept" />
                <el-table-column label="审批状态">
                  <template #default="scope">
                      <span v-if="scope.row.status=='0'">未提交</span>
                      <span v-else-if="scope.row.status=='1'">已提交</span>
                      <span v-else-if="scope.row.status=='2'">已转出</span>
                      <span v-else-if="scope.row.status=='3'">转入已接收</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作">
                  <template #default="scope">
                      <el-button type="primary" @click="approved(scope.row)" v-if="scope.row.status=='1'">同意转出</el-button>
                      <el-button type="primary" @click="accept(scope.row)" v-else-if="scope.row.status=='2'">同意接收</el-button>
                      <el-button type="primary" v-else disabled>完成账户转移</el-button>
                  </template>
                </el-table-column>
              </el-table>
  </el-row>
</template>
<script>
import request from '@/utils/request';
export default {
  data(){
      return{
          applicationList:[],
          addApplication:{status:null},
      }
  },
  mounted(){
    this.getApplication();
  },
  methods:{
      // changeInfo(row){
      //     this.dialogVisible=true;
      //     this.addApplication=row;
      // },
      addInfo(){
          this.dialogVisible=true;
          
      },
      getApplication(){
            request.get('/application/getSocialTransfer').then((res)=>{
                this.applicationList=res.data
            })
        },
        approved(row){
            request.get('/application/approvedTransfer?index='+row.index).then((res)=>{
                if(res.code==200){
                    this.getApplication();
                    this.$message({
                        type:'success',
                        message:res.msg
                    })
                }else{
                    this.getApplication();
                    this.$message({
                        type:'warning',
                        message:res.msg
                    })
                }
                console.log(res)
            })
        },
        accept(row){
            request.get('/application/acceptTransfer?index='+row.index).then((res)=>{
                console.log(res)
                if(res.code==200){
                    this.getApplication();
                    this.$message({
                        type:'success',
                        message:res.msg
                    })
                }else{
                    this.getApplication();
                    this.$message({
                        type:'warning',
                        message:res.msg
                    })
                }
            })
        },
  }
}
</script>

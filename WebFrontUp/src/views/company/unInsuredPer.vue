<template>
    <el-row>
        <el-table :data="this.laborInfoList" style="width: 100%" border>
                  <el-table-column  label="身份证号" prop="id" />
                  <!-- <el-table-column  label="姓名" prop="name" />
                  <el-table-column  label="年龄" prop="age" /> -->
                  <el-table-column  label="参加工作时间" prop="workDate" />
                  <el-table-column  label="工资" prop="salary" />
                  <el-table-column label="参保状态">
                    <template #default="scope">
                        <span v-if="scope.row.isInsurance=='true'">已参保</span>
                        <span v-else>未参保</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作">
                    <template #default="scope">
                        <el-button type="success" @click="addPayMent(scope.row)">缴费</el-button>
                        <el-button type="primary" v-if="scope.row.isInsurance=='true'" @click="getHistory(scope.row)">查看该缴费记录</el-button>
                    </template>
                  </el-table-column>
        </el-table>
    </el-row>
    <el-dialog v-model="this.dialogVisible" title="缴费" style="height: 250px;">
        <el-form v-model="this.addPayMentInfo" label-width="auto">
                  <el-form-item label="身份证号码" prop="id">
                    <el-input v-model="this.addPayMentInfo.id" />
                  </el-form-item>
                  <el-form-item label="工资" prop="salary">
                    <el-input v-model="this.addPayMentInfo.salary" disabled/>
                  </el-form-item>
                  <el-form-item label="缴纳月份" prop="insuranceDate">
                    <el-date-picker v-model="this.addPayMentInfo.insuranceDate" type="month" value-format="x" placeholder="选择缴纳时间" />
                  </el-form-item>
                </el-form>
                <el-button type="primary" style="float: right;" @click="payMent">缴费</el-button>
    </el-dialog>
    <el-dialog v-model="this.dialogHistory" title="缴费历史">
      <el-table :data="this.paymentHistoryList" style="width: 100%" border>
          <el-table-column  label="身份证号" prop="id" />
          <el-table-column  label="缴费基数" prop="paymentBase" />
          <el-table-column  label="个人缴费比例" prop="personalRate" />
          <el-table-column  label="公司缴费比例" prop="companyRate" />
          <el-table-column  label="个人缴纳" prop="personalPayments" />
          <el-table-column  label="公司缴纳" prop="companyPayments" />
          <el-table-column  label="总缴纳" prop="totalPayments" />
          <el-table-column  label="参保年月" prop="insuranceDate" />
          <el-table-column  label="缴费时间" prop="paymentDate" />
       </el-table>
    </el-dialog>    
</template>
<script>
import request from './../../utils/request.js'
export default {
    data(){
        return{
          laborInfoList:[],
          dialogVisible:false,
          addPayMentInfo:{},
          dialogHistory:false,
          paymentHistoryList:[]
        }

    },
    mounted(){
      this.getAlllaborInfo();
     
    },
    methods:{
      add0(value) {
    return value<10?'0'+value:value
        },
      formatDate(value){
        if(value == 0){
            return value;
        }
        // value *= 1000;
        var time = new Date(value);
        var y = time.getFullYear();
        var m = time.getMonth()+1;
        var d = time.getDate();
        return y+'-'+this.add0(m)+'-'+this.add0(d)},
      getAlllaborInfo(){
        request.get('/company/getAllLaborByCompany').then((res)=>{
          console.log(res);
          for(let i=0;i<res.data.length;i++){
            if(res.data[i].isSponsored=="false"){
                this.laborInfoList.push(res.data[i])
                res.data[i].workDate=this.formatDate(Number(res.data[i].workDate));
              }  
                }
         
        })
      },
      getHistory(row){
        
        request.get('/company/getPayMentById?id='+row.id).then((res)=>{
          for(let i=0;i<res.data.length;i++){
                    res.data[i].insuranceDate=this.formatDate(Number(res.data[i].insuranceDate));
                    res.data[i].paymentDate=this.formatDate(Number(res.data[i].paymentDate));
           }
          this.paymentHistoryList=res.data
        })
        this.dialogHistory=true;
      },
      addPayMent(row){
        this.dialogVisible=true
        this.addPayMentInfo.id=row.id;
        this.addPayMentInfo.salary=row.salary;
      },
      payMent(){
        request.post('/company/payMent',this.addPayMentInfo).then((res)=>{
          console.log(res);
          if(res.code==200){
            this.$message({
              type:'success',
              message:res.msg
            })
            this.dialogVisible=false;
            this.addPayMentInfo={};
            this.getAlllaborInfo();
          }else{
            this.$message({
              type:'warning',
              message:res.msg
            })
            this.getAlllaborInfo();
          }
        })
      } 
    }
}
</script>
<style>

</style>
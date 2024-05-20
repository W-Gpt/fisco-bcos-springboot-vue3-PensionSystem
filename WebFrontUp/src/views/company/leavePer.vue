<template>
    <el-row>
        <el-table :data="this.laborInfoList" style="width: 100%" border>
                  <el-table-column  label="身份证号" prop="id" />
                  <!-- <el-table-column  label="姓名" prop="name" />
                  <el-table-column  label="年龄" prop="age" /> -->
                  <el-table-column  label="参加工作时间" prop="workDate" />
                  <el-table-column  label="工资" prop="salary" />
                  <el-table-column label="操作">
                    <template #default="scope">
                        <el-button type="primary" v-if="scope.row.isInsurance=='true'" @click="getHistory(scope.row)">查看该缴费记录</el-button>
                    </template>
                  </el-table-column>
        </el-table>
    </el-row>
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
              if(res.data[i].isSponsored=="true"){
                this.laborInfoList.push(res.data[i]);
                // console.log(res.data[i]);
                res.data[i].workDate=this.formatDate(Number(res.data[i].workDate));
              }   
           }
        })
      },
      getHistory(row){
        
        request.get('/company/getPayMentById?id='+row.id).then((res)=>{
          for(let i=0;i<res.data.length;i++){
                    // res.data[i].insuranceDate=this.formatDate(Number(res.data[i].insuranceDate));
                    res.data[i].paymentDate=this.formatDate(Number(res.data[i].paymentDate));
           }
          this.paymentHistoryList=res.data
        })
        this.dialogHistory=true;
      },
    }
}
</script>
<style>

</style>
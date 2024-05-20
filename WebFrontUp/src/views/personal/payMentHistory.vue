<template>
    <el-row>
        <el-table :data="this.paymentHistoryList" style="width: 100%" border>
                  <el-table-column  label="身份证号" prop="id" />
                  <el-table-column  label="缴费基数" prop="paymentBase" />
                  <el-table-column  label="个人缴费比例(%)" prop="personalRate" />
                  <el-table-column  label="公司缴费比例(%)" prop="companyRate" />
                  <el-table-column  label="个人缴纳" prop="personalPayments" />
                  <el-table-column  label="公司缴纳" prop="companyPayments" />
                  <el-table-column  label="总缴纳" prop="totalPayments" />
                  <el-table-column  label="参保年月" prop="insuranceDate" />
                  <el-table-column  label="缴费时间" prop="paymentDate" />
                </el-table>
    </el-row>    
</template>
<script>
import request from './../../utils/request.js'
export default { 

    data(){
        return{
            paymentHistoryList:[]
        }
    },
    mounted(){
        this.getHistory();
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
        getHistory(){
        request.get('/pension/getAllPayMent').then((res)=>{
            console.log(res)
          for(let i=0;i<res.data.length;i++){
                    // res.data[i].insuranceDate=this.formatDate(Number(res.data[i].insuranceDate));
                    res.data[i].paymentDate=this.formatDate(Number(res.data[i].paymentDate));
           }
          this.paymentHistoryList=res.data
        })
      },
    }
    
}
</script>
<style>

</style>
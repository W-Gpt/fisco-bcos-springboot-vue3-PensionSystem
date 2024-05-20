<template>
    <el-row>
        <el-table :data="this.companyList">
            <el-table-column prop="companyName" label="公司名称" />
            <el-table-column prop="companyAddress" label="公司地址" />
            <el-table-column prop="personNum" label="公司员工数" />
            <el-table-column label="操作">
                <template #default="scope">
                        <el-button type="primary" @click="getHistory(scope.row)">查看详情</el-button>
                    </template>
            </el-table-column>
        </el-table>
    </el-row>
    <el-dialog v-model="dialogVisible"> 
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
    </el-dialog>
</template>
<script>
import request from './../../utils/request.js'
export default {
    data(){
        return{
            companyList:[],
            paymentHistoryList:[],
            dialogVisible:false
        }
    },
    mounted(){
        this.getAllCompany();
    },
    methods:{
        getAllCompany(){
            request.get('/socialSec/getAllCompany').then((res)=>{
                console.log(res);
                for(let i=0;i<res.data.length;i++){

                    request.get('/company/getCompanyByAddr?address='+res.data[i].companyAddress).then((resd)=>{
                    // console.log(res.data.staffSize);
                    res.data[i].personNum= resd.data.staffSize;
                    // res.data[i].personNum= 3;
                    console.log(res.data[i].personNum)
                    })  
                }
                this.companyList=res.data;
                console.log(this.companyList)
            })
        },
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
        getHistory(row){
            this.paymentHistoryList=[];
            this.dialogVisible=true;
        request.get('/socialSec/getAllPayMentByCompany?companyName='+row.companyName).then((res)=>{
            console.log(res)
            if(res.code==200){
                for(let i=0;i<res.data.length;i++){
                    // res.data[i].insuranceDate=this.formatDate(Number(res.data[i].insuranceDate));
                    res.data[i].paymentDate=this.formatDate(Number(res.data[i].paymentDate));
                }
                this.paymentHistoryList=res.data
            }else{
                this.$message({
                        type:"warning",
                         message:res.msg
                    })
            }
          
        })
      },
    }
}
</script>
<style>

</style>
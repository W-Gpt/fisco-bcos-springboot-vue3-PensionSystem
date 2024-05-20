<template>
    <el-row>
        <el-table :data="this.displayedData" style="width: 100%" border>
                  <el-table-column  label="身份证号" prop="id" />
                  <el-table-column  label="姓名" prop="name" />
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
    <el-row>
        <el-pagination
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-size="pageSize"
      :total="total"
    >
    </el-pagination>    
    </el-row>
    <!-- <Pagination :dataList="this.paymentHistoryList" @getDisplayedData="loadNewData"></Pagination> -->
</template>
<script>
import request from './../../utils/request.js'
import Pagination from '@/components/Pagination.vue';
export default { 
    components: { Pagination },
    data(){
        return{
            paymentHistoryList:[],
            displayedData : [],
            balance:null,
            total : 0,
            currentPage :1,
            pageSize : 2
        }
    },
    mounted(){
        this.getHistory(); 
        this.getCompanyInfo();
        this.loadData(this.currentPage)
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
        request.get('/company/getAllInsurance').then((res)=>{
            console.log(res)
          for(let i=0;i<res.data.length;i++){
                    res.data[i].insuranceDate=this.formatDate(Number(res.data[i].insuranceDate)).split('-').slice(0, 2).join('-');
                    res.data[i].paymentDate=this.formatDate(Number(res.data[i].paymentDate));
           }
          this.paymentHistoryList=res.data;
          this.total=this.paymentHistoryList.length;
          this.loadData(this.currentPage)
        })
      },
      loadData(newPage){
        this.displayedData=this.paymentHistoryList.slice(0+((newPage-1)*this.pageSize), this.pageSize+((newPage-1)*this.pageSize))
      },
        handleCurrentChange(newPage){
            this.currentPage = newPage;
            this.loadData(newPage);
            console.log(this.displayedData);
      },
      getCompanyInfo(){
        request.get('/company/getCompanyByAddr?address='+localStorage.getItem('userAddress')).then((res)=>{
                this.balance=res.data.balances/100;
                this.$emit('getBalance',this.balance);
            })  
      }
    }
    
}
</script>
<style>

</style>
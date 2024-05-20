<template>
    <el-row>
        <el-table :data="this.displayedData" style="width: 100%" border>
                  <el-table-column  label="身份证号" prop="id" />
                  <el-table-column  label="缴费基数" prop="paymentBase" />
                  <el-table-column  label="个人缴费比例(%)" prop="personalRate" />
                  <el-table-column  label="公司缴费比例(%)" prop="companyRate" />
                  <el-table-column  label="个人缴纳" prop="personalPayments" />
                  <el-table-column  label="公司缴纳" prop="companyPayments" />
                  <el-table-column  label="总缴纳" prop="totalPayments" />
                  <el-table-column  label="所属公司" prop="companyName" />
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
</template>
<script>
import request from './../../utils/request.js'
export default { 

    data(){
        return{
            paymentHistoryList:[],
            displayedData : [],
            total : 0,
            currentPage :1,
            pageSize : 10
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
                    res.data[i].insuranceDate=this.formatDate(Number(res.data[i].insuranceDate)).split('-').slice(0, 2).join('-');
                    res.data[i].paymentDate=this.formatDate(Number(res.data[i].paymentDate));
                    // console.log(Date.parse(res.data[i].insuranceDate));
                    
           }
          this.paymentHistoryList=res.data
        //   this.paymentHistoryList.sort((a, b) => Date.parse(a.insuranceDate) - Date.parse(b.insuranceDate));
          this.total=this.paymentHistoryList.length;
                this.loadData(this.currentPage)
        })
      },
      loadData(newPage){
        this.paymentHistoryList.sort((a, b) => Date.parse(a.insuranceDate) - Date.parse(b.insuranceDate)).reverse();
        this.displayedData=this.paymentHistoryList.slice(0+((newPage-1)*this.pageSize), this.pageSize+((newPage-1)*this.pageSize));
      },
        handleCurrentChange(newPage){
            this.currentPage = newPage;
            this.loadData(newPage);
            console.log(this.displayedData);
      }
    }
    
}
</script>
<style>

</style>
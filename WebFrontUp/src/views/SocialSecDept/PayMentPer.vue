<template>
  <el-row>
    <el-date-picker 
    v-model="selectedDate" type="month" 
    placeholder="选择查看范围"
    @change="handleDateChange">
    </el-date-picker>
      <el-table :data="filteredData">
          <el-table-column prop="id" label="身份证号" />
          <el-table-column prop="companyName" label="所在公司" />
          <el-table-column  label="缴费基数" prop="paymentBase" />
          <el-table-column  label="个人缴费比例(%)" prop="personalRate" />
          <el-table-column  label="公司缴费比例(%)" prop="companyRate" />
          <el-table-column  label="个人缴纳" prop="personalPayments" />
          <el-table-column  label="公司缴纳" prop="companyPayments" />
          <el-table-column  label="总缴纳" prop="totalPayments" />
          <el-table-column prop="insuranceDate" label="所缴月份" />
          <el-table-column prop="paymentDate" label="缴费时间" />
          
      </el-table>
  </el-row>
  <el-row>
        <el-pagination background
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
import moment from 'moment'
export default {
  data(){
      return{
        selectedDate:'',
        startDate:'',
        endDate:'',
          personList:[],
          payMentList:[],
          displayedData : [],
            total : 0,
            currentPage :1,
            pageSize : 10
        
      }
  },
  computed: {
    filteredData() {
      if (!this.selectedDate) {
        return this.displayedData; // 如果未选择时间范围，显示所有数据
      } else {
        return this.displayedData.filter(item => {
          // console.log(new Date(item.date));
          // console.log(this.startDate +'wwwwww')
          return new Date(item.insuranceDate) >= this.selectedDate && new Date(item.insuranceDate) <= this.endDate;
        });
      }
    }
  },
  mounted(){
    this.getPayMentHistroy();
  },
  methods:{
    
    handleDateChange(){
      this.startDate= new Date(this.selectedDate);
      this.endDate=this.startDate;
      this.startDate.setMonth(Number(this.startDate.getMonth())+1);
      // this.selectedDate.setMonth(Number(this.selectedDate.getMonth())+1);
    },
    getPayMentHistroy(){
      request.get('/socialSec/getAllCompanyInsurance').then((res)=>{
        for(let i=0;i<res.data.length;i++){
                // res.data[i].insuranceDate=this.formatDate(Number(res.data[i].insuranceDate));
                console.log(res.data[i].paymentDate=this.formatDate(Number(res.data[i].paymentDate)));
                // const yearMonth = dateString
                res.data[i].insuranceDate=this.formatDate(Number(res.data[i].insuranceDate)).split('-').slice(0, 2).join('-');
                
           }
           this.payMentList=res.data;
           this.total=this.payMentList.length;
                this.loadData(this.currentPage)
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
        return y+'-'+this.add0(m)+'-'+this.add0(d)
      },
      loadData(newPage){
        this.payMentList.sort((a, b) => Date.parse(a.insuranceDate) - Date.parse(b.insuranceDate)).reverse();
        this.displayedData=this.payMentList.slice(0+((newPage-1)*this.pageSize), this.pageSize+((newPage-1)*this.pageSize));
        
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
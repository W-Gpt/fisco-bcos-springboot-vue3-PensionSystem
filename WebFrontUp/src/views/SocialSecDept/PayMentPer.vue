<template>
  <el-row>
    <el-date-picker 
    v-model="selectedDate" type="month" 
    placeholder="选择查看范围"
    @change="handleDateChange">
    </el-date-picker>
      <el-table :data="filteredData">
          <el-table-column prop="id" label="身份证号" />
          <el-table-column prop="companyAddress" label="所在公司" />
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
          personList:[{date:'2024-05-05'},{date:'2024-05-05'},
          {date:'2024-06-05'},{date:'2024-06-05'},{date:'2024-02-05'},{date:'2024-03-05'},
          ],
          payMentList:[]
      }
  },
  computed: {
    filteredData() {
      if (!this.selectedDate) {
        return this.payMentList; // 如果未选择时间范围，显示所有数据
      } else {
        return this.payMentList.filter(item => {
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
                res.data[i].insuranceDate=this.formatDate(Number(res.data[i].insuranceDate));
                res.data[i].paymentDate=this.formatDate(Number(res.data[i].paymentDate));
           }
           this.payMentList=res.data;
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
  }
}
</script>
<style>

</style>
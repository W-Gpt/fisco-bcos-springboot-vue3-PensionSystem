<template>
  <el-row>
    <el-date-picker 
    v-model="selectedDate" type="month" 
    placeholder="选择查看范围"
    @change="handleDateChange">
    </el-date-picker>
      <el-table :data="filteredData">
          <el-table-column prop="name" label="姓名" />
          <el-table-column prop="id" label="身份证号" />
          <el-table-column prop="company" label="所在公司" />
          <el-table-column prop="payMentDate" label="所缴月份" />
          <el-table-column prop="date" label="缴费时间" />
          <el-table-column label="操作">
              <el-button type="primary">查看详情</el-button>
          </el-table-column>
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
          ]
      }
  },
  computed: {
    filteredData() {
      if (!this.selectedDate) {
        return this.personList; // 如果未选择时间范围，显示所有数据
      } else {
        return this.personList.filter(item => {
          // console.log(new Date(item.date));
          // console.log(this.startDate +'wwwwww')
          return new Date(item.date) >= this.selectedDate && new Date(item.date) <= this.endDate;
        });
      }
    }
  },
  mounted(){

  },
  methods:{
    handleDateChange(){
      this.startDate= new Date(this.selectedDate);
      this.endDate=this.startDate;
      this.startDate.setMonth(Number(this.startDate.getMonth())+1);
      // this.selectedDate.setMonth(Number(this.selectedDate.getMonth())+1);
    }
  }
}
</script>
<style>

</style>
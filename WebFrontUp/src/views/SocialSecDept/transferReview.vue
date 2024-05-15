<template>
  <el-date-picker v-model="date" type="date"></el-date-picker>
  <el-button @click="handleDateChange"></el-button>
</template>

<script>
import moment from 'moment';

export default {
  data() {
    return {
      date: moment(),
    };
  },
  computed: {
    newDate() {
      return moment(this.date).add(1, 'month');
    },
  },
  methods:{
    handleDateChange(){
      console.log(this.date);
    }
  }
};
</script>
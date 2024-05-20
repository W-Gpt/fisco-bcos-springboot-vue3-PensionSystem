<template>
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
import { ref } from 'vue';
export default {
    data(){
        return{
            displayedData : ref([]),
            total : 0,
            currentPage :1,
            pageSize : 2
        }
    },
    props:['dataList'],
    emits: ['getDisplayedData'], 
    mounted(){
        this.loaded();
    },
    methods:{
        loadData(newPage){
            console.log(this.dataList);
            // console.log(this.dataList.slice(0, 2));
        this.displayedData=this.dataList.slice(0+((newPage-1)*this.pageSize), this.pageSize+((newPage-1)*this.pageSize));
        // setTimeout(() => {
        //     this.$emit('getDisplayedData',this.displayedData);
        //     console.log("到");
        // }, 3000);
        this.$emit('getDisplayedData',this.displayedData);
        this.total=this.dataList.length;
        console.log("进来啦");
        console.log(this.pageSize+((newPage-1)*this.pageSize));
        console.log(this.displayedData)
      },
      loaded(){
        setTimeout( this.loadData(this.currentPage),10000);
      },
        handleCurrentChange(newPage){
            this.currentPage = newPage;
            this.loadData(newPage);
            console.log(this.displayedData);
      }
    }
}
</script>
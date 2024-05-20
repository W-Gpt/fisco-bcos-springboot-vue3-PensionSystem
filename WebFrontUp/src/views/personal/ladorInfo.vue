<template>
    <el-row>
        <el-table :data="this.displayedData" style="width: 100%" border>
                  <el-table-column  label="身份证号" prop="id" />
                  <el-table-column  label="姓名" prop="username" />
                  <el-table-column  label="所属公司" prop="companyName" />
                  <el-table-column  label="参加工作时间" prop="workDate" />
                  <el-table-column  label="工资" prop="salary" />
                  
                  <!-- <el-table-column label="是否离职">
                    <template slot-scope="scope">
                        <span v-if="scop.row.isSponsored=='true'">已离职</span>
                        <span v-else>未离职</span>
                    </template>
                  </el-table-column> -->
                  <el-table-column label="是否离职">
                    <template #default="scope">
                        <span v-if="scope.row.isSponsored=='true'">已离职</span>
                        <span v-else>未离职</span>
                        <!-- <span>nihao</span> -->
                    </template>
                  </el-table-column>
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
import request from '@/utils/request';
export default {
    data(){
      return{
        laborInfoList:[],
        displayedData : [],
            total : 0,
            currentPage :1,
            pageSize : 10
      }
    },
    mounted(){
        this.getAllLaborInfo();
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
        getAllLaborInfo(){
            request.get('/pension/getLaborInfo').then((res)=>{
                console.log(res)
                if(res.code==200){
                    for(let i=0;i<res.data.length;i++){
                    res.data[i].workDate=this.formatDate(Number(res.data[i].workDate));
                }
                this.laborInfoList=res.data
                this.total=this.laborInfoList.length;
                this.loadData(this.currentPage)
            }
            }) 
        },
        loadData(newPage){
        this.displayedData=this.laborInfoList.slice(0+((newPage-1)*this.pageSize), this.pageSize+((newPage-1)*this.pageSize));
        this.displayedData.reverse();
      },
        handleCurrentChange(newPage){
            this.currentPage = newPage;
            this.loadData(newPage);
            console.log(this.displayedData);
      }
    }
    
}
</script>
<template>
    <el-row>
        <el-col>
            <el-table :data="this.displayedData"  @row-click="addlaborDialog">
                <el-table-column prop="companyName" label="公司名称" ></el-table-column>
                <el-table-column prop="companyAddress" label="公司地址" ></el-table-column>
                <el-table-column prop="city" label="公司所在城市" ></el-table-column>
                <el-table-column prop="personNum" label="公司员工数" >
                </el-table-column>
                <el-table-column label="添加人员">
                    <el-button type="success"> 录入人员</el-button>
                </el-table-column>
            </el-table>
        </el-col>
        
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
    <el-dialog v-model="this.dialogVisible" title="添加工作信息" style="height: 380px;">
        <el-form v-model="this.laoborInfo" label-width="auto">
                  <el-form-item label="身份证号码" prop="id">
                    <el-input v-model="this.laoborInfo.id" />
                  </el-form-item>
                  <el-form-item label="公司名称" prop="companyName">
                    <el-input v-model="this.laoborInfo.companyName" disabled/>
                  </el-form-item>
                  <el-form-item label="公司地址" prop="companyAddress">
                    <el-input v-model="this.laoborInfo.companyAddress" disabled/>
                  </el-form-item>
                  <el-form-item label="工资" prop="salary">
                    <el-input v-model="this.laoborInfo.salary" />
                  </el-form-item>
                  <el-form-item label="工作时间" prop="workDate">
                    <el-date-picker v-model="this.laoborInfo.workDate" type="date" value-format="x" placeholder="选择入职时间" />
                  </el-form-item>
                </el-form>
                <el-button type="primary" style="float: right;" @click="addLaborInfo">录入人员信息</el-button>
    </el-dialog>
</template>
<script>
import request from './../../utils/request.js'
export default {
    data(){
        
        return{
            companyList:[],
            laoborInfo:{},
            dialogVisible:false,
            displayedData : [],
            total : 0,
            currentPage :1,
            pageSize : 10
        }
    },
    mounted(){
        this.getAllCompany();
    },
    methods:{
        getAllCompany(){
            request.get('/company/getAllCompany').then((res)=>{
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
                this.total=this.companyList.length;
                this.loadData(this.currentPage)
                // console.log(res);
                // this.companyList=res.data;
            })
        },
        
        addlaborDialog(row){
            this.laoborInfo.companyAddress=row.companyAddress;
            this.laoborInfo.companyName=row.companyName;
            this.dialogVisible=true;

        },
        addLaborInfo(){
            // this.laoborInfo.workDate=new Date(this.laoborInfo.workDate);
            console.log(this.laoborInfo)
            request.post('/company/addLaborInfo',this.laoborInfo).then((res)=>{
                console.log(res);
                // this.$message:
                if(res.code==200){
                    this.$message({
                        type:'success',
                        message:res.msg
                    })
                    this.dialogVisible=false;
                }else{
                    this.$message({
                    type:'warning',
                    message:res.msg
                    })
                    }
                this.laoborInfo.companyName={};
            })

        },
        loadData(newPage){
        this.displayedData=this.companyList.slice(0+((newPage-1)*this.pageSize), this.pageSize+((newPage-1)*this.pageSize));
        // this.displayedData.reverse();
      },
        handleCurrentChange(newPage){
            this.currentPage = newPage;
            this.loadData(newPage);
            console.log(this.displayedData);
      }
    }
    
}
</script>
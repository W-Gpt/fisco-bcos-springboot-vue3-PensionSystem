<template>
    <el-row>
        <el-button type="success" @click="addInfo">填写申请</el-button>
    </el-row>
    <el-row>
        <el-table :data="this.applicationList" style="width: 100%" border>
                  <el-table-column  label="身份证号" prop="id" />
                  <el-table-column  label="原公司" prop="fromCompany" />
                  <el-table-column  label="申请转入公司" prop="toCompany" />
                  <el-table-column  label="原社保局" prop="fromSocialSecDept" />
                  <el-table-column  label="转入社保局" prop="toSocialSecDept" />
                  <el-table-column label="审批状态">
                    <template #default="scope">
                        <span v-if="scope.row.status=='0'">未提交</span>
                        <span v-else-if="scope.row.status=='1'">已提交</span>
                        <span v-else-if="scope.row.status=='2'">已转出</span>
                        <span v-else-if="scope.row.status=='3'">转入已接收</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作">
                    <template #default="scope">
                        <el-button type="primary" @click="changeInfo(scope.row)" v-if="scope.row.status=='0'">修改</el-button>
                        <el-button type="primary" v-else disabled>已提交</el-button>
                        <el-button @click="addApplications(scope.row)" v-if="scope.row.status=='0'">提交</el-button>
                    </template>
                  </el-table-column>
                </el-table>
    </el-row>
    <el-dialog v-model="this.dialogVisible" style="height:300px">
        <el-form v-model="this.addApplication" label-width="auto">
            <el-form-item prop="fromCompany" label="原公司">
                <el-input v-model="this.addApplication.fromCompany" placeholder="请输入地址"></el-input>
            </el-form-item>
            <el-form-item prop="toCompany" label="转入公司">
                <el-input v-model="this.addApplication.toCompany" placeholder="请输入地址"></el-input>
            </el-form-item>
            <el-form-item prop="fromSocialSecDept" label="原社保局">
                <el-input v-model="this.addApplication.fromSocialSecDept" placeholder="请输入地址"></el-input>
            </el-form-item>
            <el-form-item prop="toSocialSecDept" label="转入社保局">
                <el-input v-model="this.addApplication.toSocialSecDept" placeholder="请输入地址"></el-input>
            </el-form-item>
            <el-button type="primary" @click="saveApplication" >添加申请</el-button>
            <el-button type="primary" @click="addApplications(this.addApplication)" style="float: right;width: 90px;">提交</el-button>
        </el-form>
    </el-dialog>   
</template>
<script>
import request from '@/utils/request';
export default {
    data(){
        return{
            applicationList:[],
            addApplication:{},
            dialogVisible:false
        }
    },
    mounted(){
        this.getApplication();
    },
    methods:{
        changeInfo(row){
            this.dialogVisible=true;
            this.addApplication=row;
        },
        addInfo(){
            this.dialogVisible=true;
            
        },
        saveApplication(){
            this.addApplication.id=localStorage.getItem('userId');
            request.post('/application/saveTransfer',this.addApplication).then((res)=>{
                console.log(res)
            })
        },
        addApplications(row){
            if(row.status !='0' ){
                this.saveApplication();
            }
            request.post('/application/applyTransfer',row).then((res)=>{
                console.log(res)
            })
            this.addApplication={};
        },
        getApplication(){
            request.get('/application/getMyTransfer?id='+localStorage.getItem('userId')).then((res)=>{
                this.applicationList=res.data
            })
        },

    }
}
</script>

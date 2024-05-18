<template>
    <el-row>
        <el-col :span="6">
            <el-button type="success" @click="dialogVisible =true">添加公司信息</el-button>
        </el-col>
    </el-row>
    <el-row>
        <el-table :data="this.companyList">
            <el-table-column prop="companyName" label="公司名称" />
            <el-table-column prop="companyAddress" label="公司地址" />
            <el-table-column prop="city" label="公司所在城市" />
            <el-table-column prop="personNum" label="公司员工数" />
            <!-- <el-table-column label="操作">
                <el-button type="primary"></el-button>
            </el-table-column> -->
        </el-table>
    </el-row>
    <el-dialog v-model="dialogVisible" title="添加公司" width="700px" style="height:300px">
        <el-form v-model="this.companyInfo" label-width="auto">
            <el-form-item prop="companyAddress" label="区块链地址">
                <el-input v-model="this.companyInfo.companyAddress" placeholder="请输入地址"></el-input>
            </el-form-item>
            <el-form-item prop="city" label="所在城市">
                <el-input v-model="this.companyInfo.city" placeholder="请输入城市名称"></el-input>
            </el-form-item>
            <el-form-item prop="name" label="公司名称">
                <el-input v-model="this.companyInfo.companyName" placeholder="请输入公司名称"></el-input>
            </el-form-item>
            <el-form-item prop="balance" label="公司注册资产">
                <el-input v-model="this.companyInfo.balance" placeholder="请输入资产"></el-input>
            </el-form-item>
            <el-button type="primary" @click="addCompany" style="float: right;width: 90px;">添加</el-button>
        </el-form>
    </el-dialog>
</template>
<script>
import request from './../../utils/request.js'
export default {
    data(){
        return{
            dialogVisible :false,
            companyInfo:{},
            companyList:[]
        }
    },
    mounted(){
        this.getAllCompany();
    },
    methods:{
        addCompany(){
            request.post('/socialSec/addCompany',this.companyInfo).then((res)=>{
                console.log(res);
                if(res.code==200){
                    this.$message({
                        type:"success",
                         message:res.msg
                    })
                    this.getAllCompany();
                }else{
                    this.$message({
                        type:"warning",
                         message:res.msg
                    })
                }
            })

            this.dialogVisible=false;
            this.companyInfo={}
        },
        getAllCompany(){
            request.get('/socialSec/getAllCompany').then((res)=>{
                console.log(res);
                this.companyList=res.data;
            })
        }
    }
}
</script>
<style>

</style>
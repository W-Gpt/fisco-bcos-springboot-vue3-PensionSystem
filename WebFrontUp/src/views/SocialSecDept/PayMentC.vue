<template>
    <el-row>
        <el-table :data="this.companyList">
            <el-table-column prop="name" label="公司名称" />
            <el-table-column prop="companyAddress" label="公司地址" />
            <el-table-column prop="personNum" label="公司员工数" />
            <el-table-column label="操作">
                <el-button type="primary">查看详情</el-button>
            </el-table-column>
        </el-table>
    </el-row>
</template>
<script>
import request from './../../utils/request.js'
export default {
    data(){
        return{
           
            companyList:[]
        }
    },
    mounted(){

    },
    methods:{
       
    }
}
</script>
<style>

</style>
<template>
    <el-row>
        <el-table :data="this.laborInfoList" style="width: 100%" border>
                  <el-table-column  label="身份证号" prop="id" />
                  <el-table-column  label="姓名" prop="name" />
                  <el-table-column  label="年龄" prop="age" />
                  <el-table-column  label="参加工作时间" prop="company" />
                  <el-table-column  label="工资" prop="salary" />
                  <el-table-column label="操作">
                    <template #default="scope">
                        <el-button @click="payMent(scope.row)">缴费</el-button>
                    </template>
                  </el-table-column>
        </el-table>
    </el-row>
    <el-dialog v-model="this.dialogVisible" title="缴费" style="height: 380px;">
        <el-form v-model="this.payMentInfo" label-width="auto">
                  <el-form-item label="身份证号码" prop="id">
                    <el-input v-model="this.payMentInfo.id" />
                  </el-form-item>
                  <el-form-item label="工资" prop="salary">
                    <el-input v-model="this.payMentInfo.salary" disabled/>
                  </el-form-item>
                  <el-form-item label="缴纳月份" prop="workDate">
                    <el-date-picker v-model="this.payMentInfo.workDate" type="month" value-format="x" placeholder="选择缴纳时间" />
                  </el-form-item>
                </el-form>
                <el-button type="primary" style="float: right;" @click="addLaborInfo">录入人员信息</el-button>
    </el-dialog>    
</template>
<script>
export default {
    data(){
        return{
            
        }

    },
    mounted(){
     
    },
    methods:{
        payMent(row){

        }
    }
}
</script>
<style>

</style>
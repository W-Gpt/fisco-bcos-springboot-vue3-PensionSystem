<template>
    <el-row>
        <el-table :data="this.laborInfoList" style="width: 100%" border>
                  <el-table-column  label="身份证号" prop="id" />
                  <el-table-column  label="姓名" prop="name" />
                  <el-table-column  label="年龄" prop="age" />
                  <el-table-column  label="工作公司" prop="company" />
                  <el-table-column  label="参加工作时间" prop="company" />
                  <el-table-column  label="工资" prop="salary" />
                </el-table>
    </el-row>    
</template>
<script>
export default { 
    data(){
        return{

        }
    }
}
</script>
<style>

</style>
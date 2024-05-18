<template>
    <el-form v-model="this.insurance" label-width="auto">
                  <el-form-item label="身份证号码" prop="id">
                    <span> {{ this.insurance.id }}</span>
                  </el-form-item>
                  <el-form-item label="所在城市" prop="city">
                    <span> {{ this.insurance.city }}</span>
                  </el-form-item>
                  <el-form-item label="个人已缴纳" prop="personalPayments">
                    <span> {{ this.insurance.personalPayments }}</span>
                  </el-form-item>
                  <el-form-item label="公司已缴纳" prop="companyPayments">
                    <span> {{ this.insurance.companyPayments }}</span>
                  </el-form-item>
                  <el-form-item label="个人已缴纳" prop="personalPayments">
                    <span> {{ this.insurance.personalPayments }}</span>
                  </el-form-item>
                  <el-form-item label="总余额" prop="totalPayments">
                    <span> {{ this.insurance.totalPayments }}</span>
                  </el-form-item>
                  <el-form-item label="最近缴费时间" prop="paymentDate">
                    <span> {{ this.insurance.paymentDate }}</span>
                  </el-form-item>
                  <el-form-item label="所属公司地址" prop="company">
                    <span> {{ this.insurance.company }}</span>
                  </el-form-item>
    </el-form>
</template>
<script>
import request from '../../utils/request'
export default {
    data(){
        return{
            insurance:{},
        }
    },
    mounted(){
        this.getMyInsurance();
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
        getMyInsurance(){
            request.get('/pension/getPensionInfo').then((res)=>{
                res.data.paymentDate=this.formatDate(Number(res.data.paymentDate));
                this.insurance=res.data;
                console.log(this.insurance.id);
                localStorage.setItem('userId',this.insurance.id);
            })
            
            
        }
    },
}
</script>
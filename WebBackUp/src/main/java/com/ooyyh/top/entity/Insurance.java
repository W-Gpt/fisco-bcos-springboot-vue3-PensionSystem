package com.ooyyh.top.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

//        uint id; //身份证
//        address companyAddress; //公司
//        address socialSecurityAddr; //社保局
//        string city; // 所在城市
//        uint paymentBase; //缴费基数
//        uint personalRate; // 个人缴费比例
//        uint companyRate; // 公司缴费比例
//        uint personalPayments; // 个人缴纳
//        uint companyPayments; //公司缴纳
//        uint totalPayments; // 总缴纳
//        uint insuranceDate;//参保年月
//        uint paymentDate;//缴费所属时间
@Data
@TableName("Insurance")
public class Insurance {
    String id;
    @TableField("company_address")
    String companyAddress;
    @TableField("social_security_addr")
    String socialSecurityAddr;
    String city;
    @TableField("payment_base")
    String paymentBase;
    @TableField("personal_rate")
    String personalRate;
    @TableField("company_rate")
    String companyRate;
    @TableField("personal_payments")
    String personalPayments;
    @TableField("company_payments")
    String companyPayments;
    @TableField("total_payments")
    String totalPayments;
    @TableField("insurance_date")
    String insuranceDate;
    @TableField("payment_date")
    String paymentDate;
    String salary;
}

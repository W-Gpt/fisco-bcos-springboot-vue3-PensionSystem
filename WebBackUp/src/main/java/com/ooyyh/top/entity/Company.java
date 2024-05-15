package com.ooyyh.top.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("company")
public class Company {
    @TableField("company_name")
    private String companyName;
    @TableField("company_address")
    private String companyAddress;
    private String city;
    private String balance;
}

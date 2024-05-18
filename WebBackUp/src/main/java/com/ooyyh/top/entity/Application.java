package com.ooyyh.top.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("application")
public class Application {
    @TableField("id")
    String id;
    @TableField("from_company")
    String fromCompany;
    @TableField("to_company")
    String toCompany;
    @TableField("from_social_sec_dept")
    String fromSocialSecDept;
    @TableField("to_social_sec_dept")
    String toSocialSecDept;
    String status;
}

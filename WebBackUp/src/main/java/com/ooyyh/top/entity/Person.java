package com.ooyyh.top.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@TableName("person")
@Data
public class Person {
    private String id;
    private String age;
    private String name;
}

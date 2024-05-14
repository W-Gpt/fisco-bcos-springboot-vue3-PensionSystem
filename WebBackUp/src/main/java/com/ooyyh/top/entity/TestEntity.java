package com.ooyyh.top.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("test")
public class TestEntity {
    private Integer id;
    private String name;
}

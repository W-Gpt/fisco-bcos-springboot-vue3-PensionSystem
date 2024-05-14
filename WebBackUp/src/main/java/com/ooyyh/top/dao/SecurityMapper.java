package com.ooyyh.top.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooyyh.top.entity.Person;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SecurityMapper extends BaseMapper<Person> {
}

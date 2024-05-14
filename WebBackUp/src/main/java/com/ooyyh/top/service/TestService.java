package com.ooyyh.top.service;

import com.ooyyh.top.dao.TestMapper;
import com.ooyyh.top.entity.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    private TestMapper testMapper;

    public List<TestEntity> getAllTest(){
        return testMapper.selectList(null);
    }

}

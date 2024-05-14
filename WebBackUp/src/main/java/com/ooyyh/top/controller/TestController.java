package com.ooyyh.top.controller;

import com.ooyyh.top.entity.TestEntity;
import com.ooyyh.top.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class TestController {
    @Autowired
    private TestService testService;
    @RequestMapping("/test")
    @ResponseBody
    public List<TestEntity> test()
    {
        return testService.getAllTest();
    }

}

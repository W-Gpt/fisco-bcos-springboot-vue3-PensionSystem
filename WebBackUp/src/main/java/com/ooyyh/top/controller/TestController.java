package com.ooyyh.top.controller;

import com.ooyyh.top.entity.TestEntity;
import com.ooyyh.top.service.TestService;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class TestController {
    @Autowired
    private TestService testService;
    @RequestMapping("/test")
    @ResponseBody
    public List<TestEntity> test() {
        return testService.getAllTest();
    }
    @GetMapping("/test1")
    @ResponseBody
    public Map test1()
    {
        return Result.success();
    }
    @GetMapping("/test2")
    @ResponseBody
    public Map test2()
    {
        return testService.generateOneYear("2022",null);
    }
    @GetMapping("/test3")
    @ResponseBody
    public Map test3(@RequestHeader String token) throws UnsupportedEncodingException {
        return testService.getAllInsurance(token);
    }

}

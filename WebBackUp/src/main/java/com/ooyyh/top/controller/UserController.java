package com.ooyyh.top.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.service.UserService;
import com.ooyyh.top.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @PostMapping(value = "/register")
    @ResponseBody
    public Map register(@RequestBody User user) {
        return userService.register(user);
    }
    @PostMapping(value = "/login")
    @ResponseBody
    public Map login(@RequestBody User user) {
       return userService.login(user);
    }

}

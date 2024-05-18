package com.ooyyh.top.controller;

import com.ooyyh.top.entity.Application;
import com.ooyyh.top.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;
    @GetMapping("/getSocialTransfer") //拿到所有转出
    @ResponseBody
    public Map getAllTransfer( @RequestHeader String token) throws Exception{
        return applicationService.getSocialTransfer(token);
    }
    @GetMapping("/getMyTransfer") //拿到我的转出
    @ResponseBody
    public Map getMyTransfer(String id) throws Exception{
        return applicationService.getMyTransfer(id);
    }
    @GetMapping("/approvedTransfer")//同意转出
    @ResponseBody
    public Map approvedTransfer(@RequestHeader String token, String index) throws Exception{
        return applicationService.approvedTransfer(token,index);
    }
    @GetMapping("/acceptTransfer") //接受转入
    @ResponseBody
    public Map acceptTransfer( @RequestHeader String token ,String index) throws Exception{
        return applicationService.acceptTransfer(token,index);
    }
    @PostMapping("/saveTransfer") //保存申请
    @ResponseBody
    public Map saveTransfer(@RequestBody Application application) throws Exception{
        return applicationService.saveTransfer(application);
    }
    @PostMapping("/applyTransfer") //发送申请
    @ResponseBody
    public Map applyTransfer( @RequestBody Application application) throws Exception{
        return applicationService.applyTransfer(application);
    }

}

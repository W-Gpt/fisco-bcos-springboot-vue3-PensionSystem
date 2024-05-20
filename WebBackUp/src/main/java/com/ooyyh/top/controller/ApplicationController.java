package com.ooyyh.top.controller;

import com.ooyyh.top.entity.Application;
import com.ooyyh.top.service.ApplicationService;
import com.ooyyh.top.util.ColorFul;
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
//    @GetMapping("/getSocialTransfer") //拿到所有转出
//    @ResponseBody
//    public Map getAllTransfer( @RequestHeader String token) throws Exception{
//        Map result = applicationService.getSocialTransfer(token);
//        ColorFul.print(result.toString(), ColorFul.RED);
//        return result;
//    }
//    @GetMapping("/getMyTransfer") //拿到我的转出
//    @ResponseBody
//    public Map getMyTransfer(String id) throws Exception{
//        return applicationService.getMyTransfer(id);
//    }
//    @GetMapping("/approvedTransfer")//同意转出
//    @ResponseBody
//    public Map approvedTransfer(@RequestHeader String token, String index) throws Exception{
//        return applicationService.approvedTransfer(token,index);
//    }
//    @GetMapping("/acceptTransfer") //接受转入
//    @ResponseBody
//    public Map acceptTransfer( @RequestHeader String token ,String index) throws Exception{
//        return applicationService.acceptTransfer(token,index);
//    }
//    @PostMapping("/saveTransfer") //保存申请
//    @ResponseBody
//    public Map saveTransfer(@RequestBody Application application) throws Exception{
//        return applicationService.saveTransfer(application);
//    }
//    @PostMapping("/applyTransfer") //发送申请
//    @ResponseBody
//    public Map applyTransfer( @RequestBody Application application) throws Exception{
//        return applicationService.applyTransfer(application);
//    }
@GetMapping("/getSocialTransfer") //拿到所有转出
@ResponseBody
public Map getAllTransfer(@RequestHeader String token) throws Exception {
    Map result = applicationService.getSocialTransfer(token);
    ColorFul.print(result.toString(), ColorFul.RED);
    return formatOutput(result);
}

    @GetMapping("/getMyTransfer") //拿到我的转出
    @ResponseBody
    public Map getMyTransfer(String id) throws Exception {
        Map result = applicationService.getMyTransfer(id);
        ColorFul.print(result.toString(), ColorFul.RED);
        return formatOutput(result);
    }

    @GetMapping("/approvedTransfer")//同意转出
    @ResponseBody
    public Map approvedTransfer(@RequestHeader String token, String index) throws Exception {
        Map result = applicationService.approvedTransfer(token, index);
        ColorFul.print(result.toString(), ColorFul.RED);
        return formatOutput(result);
    }

    @GetMapping("/acceptTransfer") //接受转入
    @ResponseBody
    public Map acceptTransfer(@RequestHeader String token, String index) throws Exception {
        Map result = applicationService.acceptTransfer(token, index);
        ColorFul.print(result.toString(), ColorFul.RED);
        return formatOutput(result);
    }

    @PostMapping("/saveTransfer") //保存申请
    @ResponseBody
    public Map saveTransfer(@RequestBody Application application) throws Exception {
        Map result = applicationService.saveTransfer(application);
        ColorFul.print(result.toString(), ColorFul.RED);
        return formatOutput(result);
    }

    @PostMapping("/applyTransfer") //发送申请
    @ResponseBody
    public Map applyTransfer(@RequestBody Application application) throws Exception {
        Map result = applicationService.applyTransfer(application);
        ColorFul.print(result.toString(), ColorFul.RED);
        return formatOutput(result);
    }

    private Map formatOutput(Map result) {
        return result;
    }



}

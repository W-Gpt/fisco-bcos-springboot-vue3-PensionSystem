package com.ooyyh.top.controller;

import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.Person;
import com.ooyyh.top.service.SecurityService;
import com.ooyyh.top.util.ColorFul;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/security")
public class SecurityController {
    @Autowired
    SecurityService securityService;
    @Autowired
    UserMapper userMapper;
//    @PostMapping(value = "/addPerson")
//    @ResponseBody
//    public Map addPerson(@RequestHeader String userAddress, @RequestBody Person person){
//        return securityService.addPerson(userAddress,person);
//    }
//    @GetMapping(value = "/getAllPerson")
//    @ResponseBody
//    public Map getAllPerson(@RequestHeader String userAddress){
//        return securityService.getAllPerson(userAddress);
//    }
    @PostMapping(value = "/addPerson")
    @ResponseBody
    public Map addPerson(@RequestHeader String userAddress, @RequestBody Person person){
        Map result = securityService.addPerson(userAddress, person);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

    @GetMapping(value = "/getAllPerson")
    @ResponseBody
    public Map getAllPerson(@RequestHeader String userAddress){
        Map result = securityService.getAllPerson(userAddress);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

}

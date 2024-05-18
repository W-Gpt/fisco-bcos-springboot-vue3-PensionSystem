package com.ooyyh.top.controller;

import com.ooyyh.top.dao.SecurityMapper;
import com.ooyyh.top.entity.Company;
import com.ooyyh.top.service.SocialSecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/socialSec")
public class SocialController {
    @Autowired
    SocialSecService socialSecService;
    @PostMapping(value = "/addCompany")
    @ResponseBody
    public Map addCompany(@RequestHeader String token, @RequestBody Company company) throws UnsupportedEncodingException {
        return socialSecService.addCompany(token,company);
    }
    @GetMapping(value = "/getAllCompany")
    @ResponseBody
    public Map getAllCompany(@RequestHeader String token) throws UnsupportedEncodingException {
        return socialSecService.getAllCompany(token);
    }
    @GetMapping(value = "/getAllPayMentByCompany")
    @ResponseBody
    public Map getAllPayMentByCompany(String companyName) throws UnsupportedEncodingException {
        return socialSecService.getAllInsurance(companyName);
    }

}

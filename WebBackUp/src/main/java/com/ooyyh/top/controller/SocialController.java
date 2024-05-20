package com.ooyyh.top.controller;

import com.ooyyh.top.dao.SecurityMapper;
import com.ooyyh.top.entity.Company;
import com.ooyyh.top.service.SocialSecService;
import com.ooyyh.top.util.ColorFul;
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
//    @PostMapping(value = "/addCompany")
//    @ResponseBody
//    public Map addCompany(@RequestHeader String token, @RequestBody Company company) throws UnsupportedEncodingException {
//        return socialSecService.addCompany(token,company);
//    }
//    @GetMapping(value = "/getAllCompany")
//    @ResponseBody
//    public Map getAllCompany(@RequestHeader String token) throws UnsupportedEncodingException {
//        return socialSecService.getAllCompany(token);
//    }
//    @GetMapping(value = "/getAllPayMentByCompany")
//    @ResponseBody
//    public Map getAllPayMentByCompany(String companyName) throws UnsupportedEncodingException {
//        return socialSecService.getAllInsurance(companyName);
//    }
    @PostMapping(value = "/addCompany")
    @ResponseBody
    public Map addCompany(@RequestHeader String token, @RequestBody Company company) throws UnsupportedEncodingException {
        Map result = socialSecService.addCompany(token, company);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

    @GetMapping(value = "/getAllCompany")
    @ResponseBody
    public Map getAllCompany(@RequestHeader String token) throws UnsupportedEncodingException {
        Map result = socialSecService.getAllCompany(token);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

    @GetMapping(value = "/getAllPayMentByCompany")
    @ResponseBody
    public Map getAllPayMentByCompany(String companyName) throws UnsupportedEncodingException {
        Map result = socialSecService.getAllInsurance(companyName);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }
    @GetMapping(value = "/getAllCompanyInsurance")
    @ResponseBody
    public Map getAllCompanyInsurance(@RequestHeader String token) throws UnsupportedEncodingException {
        Map result = socialSecService.getAllCompanyInsurance(token);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }


}

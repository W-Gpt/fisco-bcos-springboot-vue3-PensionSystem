package com.ooyyh.top.controller;

import com.ooyyh.top.entity.Insurance;
import com.ooyyh.top.entity.Labor;
import com.ooyyh.top.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;
    @RequestMapping("/addLaborInfo")
    @ResponseBody
    public Map addCompany(@RequestHeader String token, @RequestBody Labor labor) throws UnsupportedEncodingException {
        return companyService.addLaborInfo(token,labor);
    }
    @GetMapping("/getAllCompany")
    @ResponseBody
    public Map getAllCompany(@RequestHeader String token) throws UnsupportedEncodingException {
        return companyService.getAllCompanyByLabor(token);
    }
    @GetMapping("/getAllLabor")
    @ResponseBody
    public Map getAllLabor(@RequestHeader String token) throws UnsupportedEncodingException {
        return companyService.getAllLabor(token);
    }
//    @GetMapping("/getAllPayMent")
//    @ResponseBody
//    public Map getAllPayMent(@RequestHeader String token,String id) throws UnsupportedEncodingException {
//        return null;
//    }
    @GetMapping("/getAllLaborByCompany")
    @ResponseBody
    public Map getAllLaborByCompany(@RequestHeader String token) throws UnsupportedEncodingException {
        return companyService.getAllLaborByCompany(token);
    }
    @GetMapping("/getPayMentById")
    @ResponseBody
    public Map getPayMentById(@RequestHeader String token,String id) throws UnsupportedEncodingException {
        return companyService.getCompanyStaffPayment(token,id);
    }
    @PostMapping("/payMent")
    @ResponseBody
    public Map payInsurance(@RequestHeader String token, @RequestBody Insurance insurance) throws UnsupportedEncodingException {
        return companyService.payInsurance(token,insurance);
    }
    @GetMapping("/getAllInsurance")
    @ResponseBody
    public Map getAllInsurance(@RequestHeader String token) throws UnsupportedEncodingException {
        return companyService.getAllInsurance(token);
    }

}

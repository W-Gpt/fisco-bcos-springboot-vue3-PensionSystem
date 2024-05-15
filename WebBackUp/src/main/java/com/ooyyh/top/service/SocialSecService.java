package com.ooyyh.top.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.CompanyMapper;
import com.ooyyh.top.dao.InsuranceMapper;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.Company;
import com.ooyyh.top.entity.Insurance;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class SocialSecService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CompanyMapper companyMapper;
    @Autowired
    InsuranceMapper insuranceMapper;
    public Map addCompany(String token, Company company) throws UnsupportedEncodingException {
        //确定使用者是社保局
        String tokenT = URLDecoder.decode(token, "UTF-8");
        Map result = new HashMap();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User userRole = userMapper.selectOne(queryWrapper);
        QueryWrapper<Company> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("company_address",company.getCompanyAddress());
        Company companyInfo = companyMapper.selectOne(queryWrapper1);
        if (!userRole.getUserType().equals("3")){
            result.put("code","500");
            result.put("msg","用户不是社保局");
            result.put("data",null);
            return result;
        }
        if (companyInfo != null){
            result.put("code","500");
            result.put("msg","公司已存在");
            result.put("data",null);
            return result;
        }
        List funcParam = new ArrayList();
        funcParam.add(company.getCompanyAddress());
        funcParam.add(company.getCity());
        funcParam.add(company.getCompanyName());
        funcParam.add(company.getBalance());
        JSONObject data = (JSONObject) JSONObject.parse(HttpUtils.commonReq(userRole.getAddress(),"addCompany",funcParam));
        if (!(Boolean)data.get("statusOK")) {
            result.put("code","500");
            result.put("msg",data.get("message"));
            result.put("data",null);
            return result;
        } else  {
            result.put("code","200");
            result.put("msg","添加成功");
            result.put("data",null);
            companyMapper.insert(company);
            return result;

        }
    }
    public Map getAllCompany(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        Map result = new HashMap();
        List funcParam = new ArrayList();
        QueryWrapper<User> getSocialSec = new QueryWrapper<>();
        getSocialSec.eq("username", tokenT);
        User socialSec = userMapper.selectOne(getSocialSec);
        String response = HttpUtils.commonReq(socialSec.getAddress(), "getAllCompanyAddr", funcParam);
        String resolve = response.substring(3, response.length() - 3);
//        resolve = resolve.trim();
//        resolve = resolve.substring(1,resolve.length()-1);
        List<String> addressList = Arrays.asList(resolve.replace("\\\"", "").split(","));
//        List<String> addressList = Arrays.asList(AllAddress.split(","));
        List<Company> companyList = new ArrayList<>();
        for (int i = 0; i < addressList.size(); i++) {
            QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_address", addressList.get(i).trim());
            System.out.println(addressList.get(i));
            Company company = companyMapper.selectOne(queryWrapper);
            companyList.add(company);
        }
        result.put("code", "200");
        result.put("msg", "获取成功");
        result.put("data", companyList);
        return result;
    }
    public Map payInsurance(String token, Insurance insurance) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_name",tokenT);
        Company company = companyMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        funcParam.add(insurance.getId());
        funcParam.add(insurance.getSalary());
        funcParam.add(insurance.getInsuranceDate());
//        funcParam.add(insurance.getPaymentDate());
        JSONObject response = (JSONObject) JSONObject.parse(HttpUtils.commonReq(company.getCompanyAddress(),"PayMent",funcParam));
        //构建一个当前时间
        Date date = new Date();
        Long currentTime = date.getTime();
        //get社保局信息
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("city",company.getCity());
        User socialSec = userMapper.selectOne(queryWrapper1);
        insurance.setPaymentDate(currentTime.toString());//设置缴费的时间
        insurance.setCompanyAddress(company.getCompanyAddress());
        insurance.setSocialSecurityAddr(socialSec.getAddress());
        insurance.setPaymentBase(insurance.getSalary());
        insurance.setPersonalRate(socialSec.getPersonalRate());
        insurance.setCompanyRate(socialSec.getCompanyRate());
        insurance.setPersonalPayments(String.valueOf(Integer.parseInt(insurance.getSalary()) * Integer.parseInt(socialSec.getPersonalRate()) / 100));
        insurance.setCompanyPayments(String.valueOf(Integer.parseInt(insurance.getSalary()) * Integer.parseInt(socialSec.getCompanyRate()) / 100));
        insuranceMapper.insert(insurance);
        return Result.success();
    }
}

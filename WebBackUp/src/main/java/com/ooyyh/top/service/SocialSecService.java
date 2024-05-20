package com.ooyyh.top.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.CompanyMapper;
import com.ooyyh.top.dao.InsuranceMapper;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.Company;
import com.ooyyh.top.entity.Insurance;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.util.ColorFul;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
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
        if (response.equals("[\"[ ]\"]")) {
            return Result.error("这个社保局还没有公司");
        }

        String resolve = response.substring(3, response.length() - 3);
//        resolve = resolve.trim();
//        resolve = resolve.substring(1,resolve.length()-1);
        List<String> addressList = Arrays.asList(resolve.replace("\\\"", "").split(","));
//        List<String> addressList = Arrays.asList(AllAddress.split(","));
//        List<Company> companyList = new ArrayList<>();
        JSONArray allList = new JSONArray();
        for (int i = 0; i < addressList.size(); i++) {
//            QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("company_address", addressList.get(i).trim());
//            System.out.println(addressList.get(i));
//            Company company = companyMapper.selectOne(queryWrapper);
//            companyList.add(company);
            List funcParams1 = new ArrayList();
            funcParams1.add(addressList.get(i).trim());
            String response1  = HttpUtils.commonReq("getCompanyByAddr",funcParams1);
            response1 = response1.substring(2, response1.length() - 2);
            response1 = response1.replace("\"","");
            List<String> infoList = Arrays.asList(response1.split(","));
            JSONObject info = new JSONObject();
            info.put("companyAddress",infoList.get(0));
            info.put("city",infoList.get(1));
            info.put("companyName",infoList.get(2));
            info.put("balances",infoList.get(3));
            allList.add(info);
        }
//        ColorFul.print(allList.toString(),ColorFul.GREEN);
//        result.put("code", "200");
//        result.put("msg", "获取成功");
//        result.put("data", companyList);
        return Result.success(allList);
    }

    public Map getAllInsurance(String companyName) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(companyName, "UTF-8");
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_name",tokenT);
        Company company = companyMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        funcParam.add(company.getCompanyAddress());
        String response = HttpUtils.commonReq(company.getCompanyAddress(),"getCompanyPayMentAllIndex",funcParam);
        if (response.equals("[\"[ ]\"]")) {
            return Result.error("这个公司没有人缴纳社保");
        }
        response = response.substring(3, response.length() - 3);

        List<String> AllIndex = Arrays.asList(response.split(","));
        JSONArray allInsurance = new JSONArray();

        for (int i = 0; i < AllIndex.size(); i++) {
            List funcParam1 = new ArrayList();
            funcParam1.add(AllIndex.get(i).trim());
            String response1 = HttpUtils.commonReq(company.getCompanyAddress(),"getPayMentByIndex",funcParam1);
            response1 = response1.substring(2, response1.length() - 2);
            response1 = response1.replace("\"","");
            List<String> infoList = Arrays.asList(response1.split(","));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",infoList.get(0));
            jsonObject.put("companyAddress",infoList.get(1));
            jsonObject.put("socialSecurityAddr",infoList.get(2));
            jsonObject.put("city",infoList.get(3));
            jsonObject.put("paymentBase",infoList.get(4));
            jsonObject.put("personalRate",infoList.get(5));
            jsonObject.put("companyRate",infoList.get(6));
            jsonObject.put("personalPayments",Integer.parseInt(infoList.get(7)) / 100);
            jsonObject.put("companyPayments",Integer.parseInt(infoList.get(8)) / 100);
            jsonObject.put("totalPayments",Integer.parseInt(infoList.get(9)) / 100);
            //String时间戳转换为时间只要年月
            String insuranceDate = infoList.get(10).substring(0, 10);
            insuranceDate = insuranceDate.replace("-", "");
            insuranceDate = insuranceDate.substring(0, 6);
            insuranceDate = insuranceDate.substring(0, 4) + "年" + insuranceDate.substring(4, 6) + "月";
//            jsonObject.put("insuranceDate",insuranceDate);
            jsonObject.put("insuranceDate",infoList.get(10));
            jsonObject.put("paymentDate",infoList.get(11));
            allInsurance.add(jsonObject);
        }
//        ColorFul.print("该公司所有缴费记录已打出=>"+allInsurance.toString(), ColorFul.BLUE);
        return Result.success(allInsurance);

    }
    public Map getAllCompanyInsurance(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
//        Map result = new HashMap();
        List funcParam = new ArrayList();
        QueryWrapper<User> getSocialSec = new QueryWrapper<>();
        getSocialSec.eq("username", tokenT);
        User socialSec = userMapper.selectOne(getSocialSec);
        String response = HttpUtils.commonReq(socialSec.getAddress(), "getAllCompanyAddr", funcParam);
        if (response.equals("[\"[ ]\"]")) {
            return Result.error("这个社保局还没有公司");
        }
        String resolve = response.substring(3, response.length() - 3);
        List<String> addressList = Arrays.asList(resolve.replace("\\\"", "").split(","));
        JSONArray allList = new JSONArray();
        for (int i = 0; i < addressList.size(); i++) {
            List funcParam1 = new ArrayList();
            funcParam1.add(addressList.get(i).trim());
            String response1 = HttpUtils.commonReq(addressList.get(i).trim(), "getCompanyPayMentAllIndex", funcParam1);
            if (response1.equals("[\"[ ]\"]")) {
            return Result.error("这个公司没有人缴纳社保");
        }
            response1 = response1.substring(3, response1.length() - 3);
            List<String> AllIndex = Arrays.asList(response1.split(","));
            for (int j = 0; j < AllIndex.size(); j++) {
            List funcParam2 = new ArrayList();
            funcParam2.add(AllIndex.get(j).trim());
            String response2 = HttpUtils.commonReq("getPayMentByIndex",funcParam2);
            response2 = response2.substring(2, response2.length() - 2);
            response2 = response2.replace("\"","");
            List<String> infoList = Arrays.asList(response2.split(","));
            JSONObject jsonObject = new JSONObject();
            QueryWrapper<Company> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("company_address",infoList.get(1));
            Company company = companyMapper.selectOne(queryWrapper1);
            jsonObject.put("id",infoList.get(0));
            jsonObject.put("companyAddress",infoList.get(1));
            jsonObject.put("companyName",company.getCompanyName());
            jsonObject.put("socialSecurityAddr",infoList.get(2));
            jsonObject.put("city",infoList.get(3));
            jsonObject.put("paymentBase",infoList.get(4));
            jsonObject.put("personalRate",infoList.get(5));
            jsonObject.put("companyRate",infoList.get(6));
            jsonObject.put("personalPayments",Integer.parseInt(infoList.get(7)) / 100);
            jsonObject.put("companyPayments",Integer.parseInt(infoList.get(8)) / 100);
            jsonObject.put("totalPayments",Integer.parseInt(infoList.get(9)) / 100);
            jsonObject.put("insuranceDate",infoList.get(10));
            jsonObject.put("paymentDate",infoList.get(11));
            allList.add(jsonObject);
        }
//        ColorFul.print("该公司所有缴费记录已打出=>"+allList.toString(), ColorFul.BLUE);

        }
        return Result.success(allList);
    }

//    public JSONArray getAllInsurance(String companyName) throws UnsupportedEncodingException {
//        String tokenT = URLDecoder.decode(companyName, "UTF-8");
//        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("company_name",tokenT);
//        Company company = companyMapper.selectOne(queryWrapper);
//        List funcParam = new ArrayList();
//        funcParam.add(company.getCompanyAddress());
//        String response = HttpUtils.commonReq(company.getCompanyAddress(),"getCompanyPayMentAllIndex",funcParam);
//        if (response.equals("[\"[ ]\"]")) {
//            return Result.error("这个公司没有人缴纳社保");
//        }
//        response = response.substring(3, response.length() - 3);
//
//        List<String> AllIndex = Arrays.asList(response.split(","));
//        JSONArray allInsurance = new JSONArray();
//
//        for (int i = 0; i < AllIndex.size(); i++) {
//            List funcParam1 = new ArrayList();
//            funcParam1.add(AllIndex.get(i).trim());
//            String response1 = HttpUtils.commonReq(company.getCompanyAddress(),"getPayMentByIndex",funcParam1);
//            response1 = response1.substring(2, response1.length() - 2);
//            response1 = response1.replace("\"","");
//            List<String> infoList = Arrays.asList(response1.split(","));
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id",infoList.get(0));
//            jsonObject.put("companyAddress",infoList.get(1));
//            jsonObject.put("socialSecurityAddr",infoList.get(2));
//            jsonObject.put("city",infoList.get(3));
//            jsonObject.put("paymentBase",infoList.get(4));
//            jsonObject.put("personalRate",infoList.get(5));
//            jsonObject.put("companyRate",infoList.get(6));
//            jsonObject.put("personalPayments",Integer.parseInt(infoList.get(7)) / 100);
//            jsonObject.put("companyPayments",Integer.parseInt(infoList.get(8)) / 100);
//            jsonObject.put("totalPayments",Integer.parseInt(infoList.get(9)) / 100);
//            jsonObject.put("insuranceDate",infoList.get(10));
//            jsonObject.put("paymentDate",infoList.get(11));
//            allInsurance.add(jsonObject);
//        }
//        ColorFul.print("该公司所有缴费记录已打出=>"+allInsurance.toString(), ColorFul.BLUE);
//        return Result.success(allInsurance);
//
//    }


}

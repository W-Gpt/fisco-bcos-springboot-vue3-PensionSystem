package com.ooyyh.top.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.CompanyMapper;
import com.ooyyh.top.dao.InsuranceMapper;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.Company;
import com.ooyyh.top.entity.Insurance;
import com.ooyyh.top.entity.Labor;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.util.ColorFul;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Service
public class CompanyService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CompanyMapper companyMapper;
    @Autowired
    InsuranceMapper insuranceMapper;
    public Map addLaborInfo(String token, Labor labor) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
        List funcParams = new ArrayList();
        funcParams.add(labor.getId());
        funcParams.add(labor.getCompanyAddress());
        funcParams.add(labor.getWorkDate());
        funcParams.add(labor.getSalary());
        JSONObject response = (JSONObject) JSONObject.parse(HttpUtils.commonReq(user.getAddress(),"addLaborInfo",funcParams));

        if(!(Boolean)response.get("statusOK")){
            return Result.error();
        }else{
            return Result.success();
        }
    }
    public Map getAllCompany(){
//        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        List<Company> allCompany = companyMapper.selectList(null);
        return Result.success(allCompany);
    }
    public Map getAllCompanyByLabor(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
        List funcParams = new ArrayList();
        String response  = HttpUtils.commonReq(user.getAddress(),"getLaodAllCompany",funcParams);
        if (Objects.equals(response, "[\"[ ]\"]")){
            return Result.error("该社保局还没有公司哦");
        }
        String resolve = response.substring(3, response.length() - 3);
        List<String> addressList = Arrays.asList(resolve.replace("\\\"", "").split(","));
        JSONArray allList = new JSONArray();
        for (int i = 0; i < addressList.size(); i++) {
//            System.out.println("[INFO]:地址数组:" + addressList);
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
//        ColorFul.print(String.valueOf(allList),ColorFul.BLACK);
        return Result.success(allList);

    }
    public Map getAllLabor(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
        List funcParams = new ArrayList();
        String response  = HttpUtils.commonReq(user.getAddress(),"getLaodAllCompany",funcParams);
        String resolve = response.substring(3, response.length() - 3);
//        resolve = resolve.trim();
//        resolve = resolve.substring(1,resolve.length()-1);
        List<String> addressList = Arrays.asList(resolve.replace("\\\"", "").split(","));
        List<Labor> allLabor = new ArrayList<>();
        for (int i = 0; i < addressList.size(); i++) {
//            System.out.println("[INFO]:地址数组:" + addressList);
            List funcParam = new ArrayList();
            funcParam.add(addressList.get(i).trim());
//            System.out.println(HttpUtils.commonReq("getCompanyAllper", funcParam));
            String all = HttpUtils.commonReq("getCompanyAllper", funcParam);
//            System.out.println("[INFO]:AllPer:"+all);
            if (Objects.equals(all, "[\"[ ]\"]")){
//                System.out.println("\u001B[34m[INFO]: 本轮进入 Continue\u001B[0m");
                continue;
            }
            List<String> allPer = Arrays.asList(all.substring(3, all.length() - 3).split(","));
//            System.out.println("AllPer:"+allPer);
//            ColorFul.print("[INFO]:本轮AllPer"+allPer, ColorFul.BLUE);
//            System.out.println("[INFO]:本轮get(0)"+allPer.get(0));
//            if (allPer.get(0) == null){
//                System.out.println("[INFO]:"+allPer.get(0)+"本轮进入continue");
//                continue;
//            }
//            String response2 = response1.substring(3, response1.length() - 3);
//            System.out.println(response2);
//            List<String> allPer = Arrays.asList(response2.split(","));
//            System.out.println(allPer);
//            List<String> allPer = Arrays.asList(HttpUtils.commonReq("getCompanyAllper", funcParam).substring(3, response.length() - 3).split(","));
            for (int j = 0; j < allPer.size(); j++) {
//                System.out.println("[INFO]:"+allPer.get(j));
//                System.out.println(allPer.get(j));
                List funcParam1 = new ArrayList();
                funcParam1.add(allPer.get(j).trim());
                String info = HttpUtils.commonReq("getLaborInfo",funcParam1);
                info = info.substring(1,info.length()-1);
                info = info.replace("\"","");
                List<String> infoList = Arrays.asList(info.split(","));
//                System.out.println("[INFO]:"+info);
                allLabor.add(new Labor(infoList.get(0),infoList.get(1),infoList.get(2),infoList.get(3),infoList.get(4),infoList.get(5),infoList.get(6),infoList.get(7)));
            }
        }

        return Result.success(allLabor);
    }
//    public Map getCompanyStaffs(String token) throws UnsupportedEncodingException {
//        String tokenT = URLDecoder.decode(token, "UTF-8");
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//    }
    public Map payInsurance(String token, Insurance insurance) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_name",tokenT);
        Company company = companyMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        Date date = new Date();
        Long currentTime = date.getTime();
        insurance.setPaymentDate(currentTime.toString());
        funcParam.add(insurance.getId());
        funcParam.add(insurance.getSalary());
        funcParam.add(insurance.getInsuranceDate());
        funcParam.add(insurance.getPaymentDate());
        JSONObject response = (JSONObject) JSONObject.parse(HttpUtils.commonReq(company.getCompanyAddress(),"PayMent",funcParam));
        //构建一个当前时间
        ColorFul.print(String.valueOf(response),ColorFul.RED);
        if (!(Boolean)response.get("statusOK")) {
            return Result.error((String) response.get("message"));

        } else  {
            return Result.success();
        }

//        //get社保局信息
//        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
//        queryWrapper1.eq("city",company.getCity());
//        User socialSec = userMapper.selectOne(queryWrapper1);
//        insurance.setPaymentDate(currentTime.toString());//设置缴费的时间
//        insurance.setCompanyAddress(company.getCompanyAddress());
//        insurance.setSocialSecurityAddr(socialSec.getAddress());
//        insurance.setPaymentBase(insurance.getSalary());
//        insurance.setPersonalRate(socialSec.getPersonalRate());
//        insurance.setCompanyRate(socialSec.getCompanyRate());
//        insurance.setPersonalPayments(String.valueOf(Integer.parseInt(insurance.getSalary()) * Integer.parseInt(socialSec.getPersonalRate()) / 100));
//        insurance.setCompanyPayments(String.valueOf(Integer.parseInt(insurance.getSalary()) * Integer.parseInt(socialSec.getCompanyRate()) / 100));
//        insuranceMapper.insert(insurance);
//        return Result.success();
    }


    public Map generateOneYear(String time) {
        Map<String, List<Insurance>> oneYear = new HashMap<>();
        //time是20246截取年份生成12个月的数组
        String year = time.substring(0, 4);
        for (int i = 0; i < 13; i++) {
            oneYear.put(year + i, null);
        }
        return oneYear;
    }
    public Map getAllInsurance(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_name",tokenT);
        Company company = companyMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        funcParam.add(company.getCompanyAddress());
        String response = HttpUtils.commonReq(company.getCompanyAddress(),"getCompanyPayMentAllIndex",funcParam);
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
            QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id",infoList.get(0));
            User user = userMapper.selectOne(queryWrapper1);
            jsonObject.put("id",infoList.get(0));
            jsonObject.put("name",user.getUsername());
            jsonObject.put("companyAddress",infoList.get(1));
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
            allInsurance.add(jsonObject);
        }
//        ColorFul.print("该公司所有缴费记录已打出=>"+allInsurance.toString(), ColorFul.BLUE);
        return Result.success(allInsurance);

    }
    public Map getAllLaborByCompany(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_name",tokenT);
        Company company = companyMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        funcParam.add(company.getCompanyAddress());
        String response = HttpUtils.commonReq(company.getCompanyAddress(),"getCompanyAllper",funcParam);
        response = response.substring(3, response.length() - 3);
        List<String> AllIndex = Arrays.asList(response.split(","));
        JSONArray allLabor = new JSONArray();
        for (int i = 0; i < AllIndex.size(); i++) {
            List funcParam1 = new ArrayList();
            funcParam1.add(AllIndex.get(i).trim());
            String response1 = HttpUtils.commonReq(company.getCompanyAddress(),"getLaborInfo",funcParam1);
            response1 = response1.substring(2, response1.length() - 2);
            response1 = response1.replace("\"","");
            List<String> infoList = Arrays.asList(response1.split(","));
            JSONObject jsonObject = new JSONObject();
            QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id",infoList.get(0));
            User user = userMapper.selectOne(queryWrapper1);
            List funcParam2 = new ArrayList();
            funcParam2.add(infoList.get(2).trim());
            String socialSec = HttpUtils.commonReq("getSocialAddr",funcParam2);
            socialSec = socialSec.substring(2, socialSec.length() - 2);
            socialSec = socialSec.replace("\"","");
            List<String> socialSecList = Arrays.asList(socialSec.split(","));
            jsonObject.put("maxBase",socialSecList.get(0));
            jsonObject.put("minBase",socialSecList.get(1));
            jsonObject.put("personalRate",socialSecList.get(2));
            jsonObject.put("companyRate",socialSecList.get(3));
            jsonObject.put("id",infoList.get(0));
            jsonObject.put("name",user.getUsername());
            jsonObject.put("companyAddress",infoList.get(1));
            jsonObject.put("city",infoList.get(2));
            jsonObject.put("workDate",infoList.get(3));
            jsonObject.put("salary",infoList.get(4));
            jsonObject.put("isInsurance",infoList.get(5));
            jsonObject.put("isSponsored",infoList.get(6));
            jsonObject.put("SeparationDate",infoList.get(7));
            allLabor.add(jsonObject);
        }
        //[DEBUG]
//        ColorFul.print(String.valueOf(allLabor),ColorFul.BLUE);
        return Result.success(allLabor);
    }
    public Map getCompanyStaffPayment(String token,String id) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_name",tokenT);
        Company company = companyMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        funcParam.add(company.getCompanyAddress());
        funcParam.add(id);
        String response = HttpUtils.commonReq(company.getCompanyAddress(),"getCompanyStaffIndex",funcParam);
        response = response.substring(3, response.length() - 3);
        List<String> AllIndex = Arrays.asList(response.split(","));
        JSONArray allLabor = new JSONArray();
        for (int i = 0; i < AllIndex.size(); i++) {
            List funcParam1 = new ArrayList();
//            funcParam1.add(company.getCompanyAddress());
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
            jsonObject.put("insuranceDate",infoList.get(10));
            jsonObject.put("paymentDate",infoList.get(11));
            allLabor.add(jsonObject);

        }
//        ColorFul.print(String.valueOf(allLabor),ColorFul.BLUE);
        return Result.success(allLabor);

    }
    public Map getCompanyByAddr(String address) {
        List funcParam = new ArrayList();
        funcParam.add(address);
        String response  = HttpUtils.commonReq("getCompanyByAddr",funcParam);
        response = response.substring(2, response.length() - 2);
        response = response.replace("\"","");
        List<String> infoList = Arrays.asList(response.split(","));
        JSONObject info = new JSONObject();
        info.put("companyAddress",infoList.get(0));
        info.put("city",infoList.get(1));
        info.put("companyName",infoList.get(2));
        info.put("balances",infoList.get(3));
        info.put("staffSize",infoList.get(4));
        return Result.success(info);
    }
}




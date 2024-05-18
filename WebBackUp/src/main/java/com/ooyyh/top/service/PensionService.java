package com.ooyyh.top.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.util.ColorFul;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class PensionService {
    @Autowired
    UserMapper userMapper;
    public Map getPensionInfo(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        funcParam.add(user.getId());
        String pensionInfo = HttpUtils.commonReq("getPensionInfo",funcParam);
        pensionInfo = pensionInfo.substring(2,pensionInfo.length()-2);
        pensionInfo = pensionInfo.replace("\"","");
        ColorFul.print(pensionInfo,ColorFul.GREEN);
        List<String> infoList = Arrays.asList(pensionInfo.split(","));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",infoList.get(0));
        jsonObject.put("city",infoList.get(1));
        jsonObject.put("personalPayments",Integer.parseInt(infoList.get(2)) /100);
        jsonObject.put("companyPayments",Integer.parseInt(infoList.get(3)) /100);
        jsonObject.put("totalPayments",Integer.parseInt(infoList.get(4)) / 100);
        jsonObject.put("paymentDate",infoList.get(5));
        jsonObject.put("company",infoList.get(6));
        ColorFul.print(jsonObject.toJSONString(),ColorFul.BLUE);
        return Result.success(jsonObject);
    }
    public Map getAllPayMent(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        funcParam.add(user.getId());
        String pensionInfo = HttpUtils.commonReq("getPayMentById",funcParam);
        pensionInfo = pensionInfo.substring(3,pensionInfo.length()-3);
        List<String> AllIndex = Arrays.asList(pensionInfo.split(","));
        JSONArray allList = new JSONArray();
        for (int i = 0; i < AllIndex.size(); i++) {
            List funcParam1 = new ArrayList();
            funcParam1.add(AllIndex.get(i).trim());
            String paymentInfo = HttpUtils.commonReq("getPayMentByIndex",funcParam1);
            paymentInfo = paymentInfo.substring(2,paymentInfo.length()-2);
            paymentInfo = paymentInfo.replace("\"","");
            List<String> infoList = Arrays.asList(paymentInfo.split(","));
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
            allList.add(jsonObject);
        }
        ColorFul.print(allList.toJSONString(),ColorFul.BLUE);
        return Result.success(allList);
    }

    public Map getLaborIndexPer(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        funcParam.add(user.getId());
        String pensionInfo = HttpUtils.commonReq("getLaborIndexPer",funcParam);
        pensionInfo = pensionInfo.substring(3,pensionInfo.length()-3);
        List<String> AllIndex = Arrays.asList(pensionInfo.split(","));
        JSONArray allList = new JSONArray();
        for (int i = 0; i < AllIndex.size(); i++) {
            List funcParam1 = new ArrayList();
            funcParam1.add(AllIndex.get(i).trim());
            String paymentInfo = HttpUtils.commonReq("getLaborInfo",funcParam1);
            paymentInfo = paymentInfo.substring(2,paymentInfo.length()-2);
            paymentInfo = paymentInfo.replace("\"","");
            List<String> infoList = Arrays.asList(paymentInfo.split(","));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",infoList.get(0));
            jsonObject.put("companyAddress",infoList.get(1));
            jsonObject.put("city",infoList.get(2));
            jsonObject.put("workDate",infoList.get(3));
            jsonObject.put("salary",infoList.get(4));
            jsonObject.put("isInsurance",infoList.get(5));
            jsonObject.put("isSponsored",infoList.get(6));
            jsonObject.put("username",user.getUsername());
            allList.add(jsonObject);


        }
        ColorFul.print(allList.toJSONString(),ColorFul.BLUE);
        return Result.success(allList);

    }
}

package com.ooyyh.top.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ooyyh.top.dao.ApplicationMapper;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.Application;
import com.ooyyh.top.entity.Company;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.util.ColorFul;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {
    @Autowired
    ApplicationMapper applicationMapper;
    @Autowired
    UserMapper userMapper;
    public Map saveTransfer(Application application){
        application.setStatus("0");
        applicationMapper.insert(application);
        return Result.success();

    }
    public Map getAllTransfer(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
        QueryWrapper<Application> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id_number",user.getId());
        List <Application> allTransfer = applicationMapper.selectList(queryWrapper1);
        return Result.success(allTransfer); //ok了
    }
//    public Map applyTransfer(String id){
//        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id",id);
//        Application application = applicationMapper.selectOne(queryWrapper);
//        if (application == null) {
//            return Result.error("该申请暂未保存!");
//        }
//        List funcParam = new ArrayList();
//        funcParam.add(application.getId());
//        funcParam.add(application.getFromCompany());
//        funcParam.add(application.getToCompany());
//        funcParam.add(application.getFromSocialSecDept());
//        funcParam.add(application.getToSocialSecDept());
//        JSONObject data = (JSONObject) JSONObject.parse(HttpUtils.commonReq("applyTransfer",funcParam));
//        if (!(Boolean)data.get("statusOK")){
//            return Result.error();
//        } else {
//            application.setStatus("1"); //1代表已申请
//            applicationMapper.updateById(application);
//            return Result.success();
//        }
//
//    }
    public Map<String, Object> applyTransfer(Application application) {
        // 检查申请是否存在
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", application.getId());
        Application existingApplication = applicationMapper.selectOne(queryWrapper);
        if (existingApplication == null) {
            return Result.error("未找到对应的申请记录!");
        }

        // 准备转移请求参数
        List<Object> transferParams = new ArrayList<>();
        transferParams.add(application.getId());
        transferParams.add(application.getFromCompany());
        transferParams.add(application.getToCompany());
        transferParams.add(application.getFromSocialSecDept());
        transferParams.add(application.getToSocialSecDept());

        // 发送转移请求
        JSONObject transferResult = (JSONObject) JSONObject.parse(HttpUtils.commonReq("applyTransfer", transferParams));
        if (!(Boolean) transferResult.get("statusOK")) {
            return Result.error("转移申请失败!");
        } else {
            // 更新申请状态为已申请
            application.setStatus("1"); // "1" 代表已申请
            // 更新申请记录
            UpdateWrapper<Application> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("from_company", application.getFromCompany()).and(i -> i.eq("to_company", application.getToCompany()));
            applicationMapper.update(application, updateWrapper);
            return Result.success("转移申请成功!");
        }
    }



    public Map approvedTransfer(String token,String index) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
//        if (application == null) {
//            return Result.error("该申请暂未保存!");
//        }
        List funcParam = new ArrayList();
        funcParam.add(index);
        JSONObject data = (JSONObject) JSONObject.parse(HttpUtils.commonReq(user.getAddress(),"approvedTransfer",funcParam));
        if (!(Boolean)data.get("statusOK")){
            return Result.error();
        } else {
            QueryWrapper<Application> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("status","1");
            Application application = applicationMapper.selectOne(queryWrapper1);
            application.setStatus("2");
            applicationMapper.update(application,queryWrapper1);
            return Result.success();
        }

    }
    public Map acceptTransfer(String token,String index) throws UnsupportedEncodingException {
//        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id",id);
//        Application application = applicationMapper.selectOne(queryWrapper);
//        if (application == null) {
//            return Result.error("该申请暂未保存!");
//        }
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
//        if (application == null) {
//            return Result.error("该申请暂未保存!");
//        }
        List funcParam = new ArrayList();
        funcParam.add(index);
        JSONObject data = (JSONObject) JSONObject.parse(HttpUtils.commonReq(user.getAddress(),"acceptTransfer",funcParam));
        if (!(Boolean)data.get("statusOK")){
            return Result.error();
        } else {
            QueryWrapper<Application> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("status","2");
            Application application = applicationMapper.selectOne(queryWrapper1);
            application.setStatus("3");
            applicationMapper.update(application,queryWrapper1);
            return Result.success();
        }

    }
//    public Map getMyTransfer(String token) throws UnsupportedEncodingException {
//        String tokenT = URLDecoder.decode(token, "UTF-8");
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username",tokenT);
//        User user = userMapper.selectOne(queryWrapper);
//        List funcParam = new ArrayList();
//        funcParam.add(user.getId());
//        String response = HttpUtils.commonReq("getOwnerApplicationIndex",funcParam);
//        response = response.substring(3,response.length()-3);
//        List<String> responseList = Arrays.asList(response.split(","));
//        JSONArray allList = new JSONArray();
//        for (int i = 0; i < responseList.size(); i++) {
//            List funcParam1 = new ArrayList();
//            funcParam1.add(responseList.get(i).trim());
//            String response1 = HttpUtils.commonReq("getApplicationInfo",funcParam1);
//            response1 = response1.substring(2,response1.length()-2);
//            response1 = response1.replace("\"", "");
//            List<String> data = Arrays.asList(response1.split(","));
//            JSONObject application = new JSONObject();
//            application.put("id",data.get(0));
//            application.put("fromCompany",data.get(1));
//            application.put("toCompany",data.get(2));
//            application.put("fromSocialSecDept",data.get(3));
//            application.put("toSocialSecDept",data.get(4));
//            application.put("status",data.get(5));
//            allList.add(application);
//        }
//        ColorFul.print("所有跳槽信息" + allList.toString() ,ColorFul.GREEN);
//        return Result.success(allList);
//
//    }
    public Map getMyTransfer(String id) throws UnsupportedEncodingException {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        List<Application> allAp = applicationMapper.selectList(queryWrapper);
        if (allAp.size() == 0) {
            return Result.error("该用户暂未申请!");
        } else {
            return Result.success(allAp);
        }


    }

    public Map getSocialTransfer(String token) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",tokenT);
        User user = userMapper.selectOne(queryWrapper);
        List funcParam = new ArrayList();
        funcParam.add(user.getAddress());
        String response = HttpUtils.commonReq("getSocialApplicationIndex",funcParam);
        response = response.substring(3,response.length()-3);
        List<String> responseList = Arrays.asList(response.split(","));
        JSONArray allList = new JSONArray();
        for (int i = 0; i < responseList.size(); i++) {
            List funcParam1 = new ArrayList();
            funcParam1.add(responseList.get(i).trim());
            String response1 = HttpUtils.commonReq("getApplicationInfo",funcParam1);
            response1 = response1.substring(2,response1.length()-2);
            response1 = response1.replace("\"", "");
            List<String> data = Arrays.asList(response1.split(","));
            JSONObject application = new JSONObject();
            application.put("id",data.get(0));
            application.put("fromCompany",data.get(1));
            application.put("toCompany",data.get(2));
            application.put("fromSocialSecDept",data.get(3));
            application.put("toSocialSecDept",data.get(4));
            application.put("status",data.get(5));
            application.put("index",responseList.get(i).trim());
            allList.add(application);
        }
        ColorFul.print("所有跳槽信息" + allList.toString() ,ColorFul.GREEN);
        return Result.success(allList);

    }
    public Map getMyTs(String id){
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        List<Application> applications = applicationMapper.selectList(queryWrapper);
        return Result.success(applications);
    }
}

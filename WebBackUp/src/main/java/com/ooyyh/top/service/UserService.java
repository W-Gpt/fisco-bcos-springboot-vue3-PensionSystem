package com.ooyyh.top.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.CompanyMapper;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.Company;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CompanyMapper companyMapper;
    public Map register( User user) {
        // switch user.getUserType 1用户 2劳保局 3社保局
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        if (userMapper.selectOne(queryWrapper) != null) {
            Map result = new HashMap();
            result.put("code","500");
            result.put("msg","用户名已存在");
            result.put("data",null);
            return result;
        }
        List funcParam = new ArrayList();
        if ("1".equals(user.getUserType())) {
            funcParam.add(user.getId());
            String data = HttpUtils.commonReq("addPenSionAccount", funcParam);
            JSONObject responseData = (JSONObject) JSONObject.parse(data);
            System.out.println(responseData.get("transactionIndex"));
            Map result = new HashMap();
            if (!(Boolean)responseData.get("statusOK")){
                result.put("code","500");
                result.put("msg",responseData.get("message"));
                result.put("data",null);
                return result;
            } else {
                result.put("code","200");
                result.put("msg","注册成功");
                result.put("data",null);
                userMapper.insert(user);
                return result;
            }
        } else if ("2".equals(user.getUserType())) {
            funcParam.add(user.getAddress());
            funcParam.add(user.getCity());
            String data = HttpUtils.commonReq("regLaodongRoles", funcParam);
            JSONObject responseData = (JSONObject) JSONObject.parse(data);
            Map result = new HashMap();
            if (!(Boolean)responseData.get("statusOK")){
                result.put("code","500");
                result.put("msg",responseData.get("message"));
                result.put("data",null);
                return result;
            } else {
                result.put("code","200");
                result.put("msg","注册成功");
                result.put("data",null);
                userMapper.insert(user);
                return result;
            }
        } else if ("3".equals(user.getUserType())) {
            funcParam.add(user.getCity());
            funcParam.add(user.getAddress());
            funcParam.add(user.getMaxBase());
            funcParam.add(user.getMinBase());
            funcParam.add(user.getPersonalRate());
            funcParam.add(user.getCompanyRate());
            String data = HttpUtils.commonReq("addSocialSecDept", funcParam);
            JSONObject responseData = (JSONObject) JSONObject.parse(data);
            Map result = new HashMap();
            if (!(Boolean)responseData.get("statusOK")){
                result.put("code","500");
                result.put("msg",responseData.get("message"));
                result.put("data",null);
                return result;
            } else {
                result.put("code","200");
                result.put("msg","注册成功");
                result.put("data",null);
                userMapper.insert(user);
                return result;
            }
        } else {
            // 处理未知的用户类型
            throw new IllegalArgumentException("未知的用户类型: " + user.getUserType());
        }

    }
    public Map login(User user){
        Map result = new HashMap();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername()); //首先有这个人
        User info = userMapper.selectOne(queryWrapper);
        if (Objects.equals(user.getUserType(), "5")) {
            QueryWrapper<Company> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("company_name",user.getUsername());
            Company company = companyMapper.selectOne(queryWrapper1);
            if (Objects.equals(company.getCompanyAddress(), user.getAddress())) {
                return Result.success();
            } else {
                return Result.error();
            }
        }else {
            if (info != null) {
                if (info.getUserType().equals(user.getUserType()) ) {
                    if (info.getPassword().equals(user.getPassword())) {
                        result.put("code","200");
                        result.put("msg","登录成功");
                        result.put("data",info);
                        return result;
                    } else {
                        result.put("code","500");
                        result.put("msg","密码错误");
                        result.put("data",null);
                        return result;
                    }
                } else {
                    result.put("code","500");
                    result.put("msg","用户类型错误");
                    result.put("data",null);
                    return result;
                }
            } else {
                result.put("code","500");
                result.put("msg","用户不存在");
                result.put("data",null);
                return result;
            }

        }

    }

}

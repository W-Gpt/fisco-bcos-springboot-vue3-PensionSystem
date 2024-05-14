package com.ooyyh.top.controller;

import com.alibaba.fastjson.JSONObject;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserMapper userMapper;
    @PostMapping(value = "/register")
    @ResponseBody
    public Map register(@RequestBody User user) {
        // switch user.getUserType 1用户 2劳保局 3社保局
        List funcParam = new ArrayList();
        if ("1".equals(user.getUserType())) {
            funcParam.add(user.getId());
            String data = HttpUtils.commonReq("addPenSionAccount", funcParam);
            JSONObject responseData = (JSONObject) JSONObject.parse(data);
            System.out.println(responseData.get("transactionIndex"));
            Map result = new HashMap();
            if (responseData.get("transactionIndex") == "0x0"){
                result.put("code","500");
                result.put("msg",responseData.get("message"));
                result.put("data",null);
                return result;
            } else if (responseData.get("transactionIndex") == "0x1"){
                result.put("code","200");
                result.put("msg","注册成功");
                result.put("data",null);
                return result;
            }
        } else if ("2".equals(user.getUserType())) {
            funcParam.add(user.getLaborAddress());
            funcParam.add(user.getCity());
            String data = HttpUtils.commonReq("regLaodongRoles", funcParam);
            JSONObject responseData = (JSONObject) JSONObject.parse(data);
            Map result = new HashMap();
            if (responseData.get("transactionIndex") == "0x0"){
                result.put("code","500");
                result.put("msg",responseData.get("message"));
                result.put("data",null);
                return result;
            } else if (responseData.get("transactionIndex") == "0x1"){
                result.put("code","200");
                result.put("msg","注册成功");
                result.put("data",null);
                return result;
            }
        } else if ("3".equals(user.getUserType())) {
            funcParam.add(user.getCity());
            funcParam.add(user.getSocialAddress());
            funcParam.add(user.getMaxBase());
            funcParam.add(user.getMinBase());
            funcParam.add(user.getPersonalRate());
            funcParam.add(user.getCompanyRate());
            String data = HttpUtils.commonReq("addSocialSecDept", funcParam);
            JSONObject responseData = (JSONObject) JSONObject.parse(data);
            Map result = new HashMap();
            if (responseData.get("transactionIndex") == "0x0"){
                result.put("code","500");
                result.put("msg",responseData.get("message"));
                result.put("data",null);
                return result;
            } else if (responseData.get("transactionIndex") == "0x1"){
                result.put("code","200");
                result.put("msg","注册成功");
                result.put("data",null);
                return result;
            }
        } else {
            // 处理未知的用户类型
            throw new IllegalArgumentException("未知的用户类型: " + user.getUserType());
        }
        return null;
    }

}

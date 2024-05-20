package com.ooyyh.top.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.SecurityMapper;
import com.ooyyh.top.entity.Person;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SecurityService {
    @Autowired
    SecurityMapper securityMapper;
    public Map addPerson(String userAddress, Person person) {
        Map result = new HashMap();
        List funcParam = new ArrayList();
        funcParam.add(person.getId());
        funcParam.add(person.getAge());
        funcParam.add(person.getName());
        JSONObject data = (JSONObject) JSONObject.parse(HttpUtils.commonReq(userAddress,"addPerson",funcParam));
        if (!(Boolean)data.get("statusOK")) {
            result.put("code","500");
            result.put("msg",data.get("message"));
            result.put("data",null);
            return result;
        } else  {
            result.put("code","200");
            result.put("msg","添加成功");
            result.put("data",null);
            securityMapper.insert(person);
            return result;
        }
    }
//    public List<Person> getAllPerson(String userAddress) {
//        List<Person> userList = new ArrayList<>();
//        List<Object> funcParam = new ArrayList<>();
//        String data = HttpUtils.commonReq(userAddress, "getAllPersonId", funcParam);
//        System.out.println(data); // data => ["[1,2]"]
//
//        // 去掉外层的 [" 和 "]
//        if (data.startsWith("[\"") && data.endsWith("\"]")) {
//            data = data.substring(2, data.length() - 2);
//        }
//
//        // 去掉内部的方括号并拆分字符串
//        data = data.replaceAll("[\\[\\]]", "").trim();
//        String[] elements = data.split(",");
//
//        // 将字符串数组转换为整数列表
//        List<Integer> idList = new ArrayList<>();
//        for (String element : elements) {
//            try {
//                idList.add(Integer.parseInt(element.trim()));
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println(idList); // 输出 idList 以确保正确解析
//
//        // 使用 idList 进行查询并填充 userList
//        for (Integer id : idList) {
//            QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("id", id);
//            Person person = securityMapper.selectOne(queryWrapper);
//            userList.add(person);
//        }
//
//        return userList;
//    }

    public Map getAllPerson(String userAddress) {
        Map result = new HashMap();
        List<Person> userList = new ArrayList<Person>();
        List funcParam = new ArrayList();
        String data = HttpUtils.commonReq(userAddress,"getAllPersonId",funcParam);
//        System.out.println(data); // data => [1,2]
        List<String> idList = Arrays.asList((data.substring(3, data.length() - 3)).split(","));
//        System.out.println(idList.get());
        for (int i = 0; i < idList.size(); i++) {
//            System.out.println(i);
            QueryWrapper<Person> queryWrapper = new QueryWrapper<Person>();
            queryWrapper.eq("id",idList.get(i));
            Person onUser = securityMapper.selectOne(queryWrapper);
            userList.add(onUser);
        }
        result.put("code","200");
        result.put("msg","查询成功");
        result.put("data",userList);
        return result;


    }
}

package com.ooyyh.top.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.CompanyMapper;
import com.ooyyh.top.dao.TestMapper;
import com.ooyyh.top.entity.Company;
import com.ooyyh.top.entity.Insurance;
import com.ooyyh.top.entity.TestEntity;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Service
public class TestService {
    @Autowired
    private TestMapper testMapper;
    @Autowired
    CompanyMapper companyMapper;

    public List<TestEntity> getAllTest(){
        return testMapper.selectList(null);
    }
    public Map generateOneYear(String time, Insurance insurance) {
        Map<String, Object> oneYear = new HashMap<>();
        //time是202406/截取年份
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
            funcParam1.add(AllIndex.get(i));
            String response1 = HttpUtils.commonReq(company.getCompanyAddress(),"getPayMentByIndex",funcParam1);
            response1 = response1.substring(3, response1.length() - 3);
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
            jsonObject.put("personalPayments",infoList.get(7));
            jsonObject.put("companyPayments",infoList.get(8));
            jsonObject.put("totalPayments",infoList.get(9));
            jsonObject.put("insuranceDate",infoList.get(10));
            jsonObject.put("paymentDate",infoList.get(11));
            allInsurance.add(jsonObject);
        }
        return Result.success(allInsurance);

    }

}

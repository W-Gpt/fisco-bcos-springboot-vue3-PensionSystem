package com.ooyyh.top;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        JSONArray array=new JSONArray();
        array.add(0,"a");
        array.add(1,"b");
        array.add(2,"c");
        List<String> list = JSONObject.parseArray(array.toJSONString(),String.class);
        System.out.println(list.toString());
    }

}

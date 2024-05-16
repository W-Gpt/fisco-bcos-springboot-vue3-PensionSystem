package com.ooyyh.top.util;
import com.ooyyh.top.entity.Insurance;
import java.util.HashMap;
import java.util.Map;

public class GenerateOneYear {
    Map<String, Object> oneYear = new HashMap<>();

    public Map generateOneYear(String time, Insurance insurance) {
        //time是202406/截取年份
        String year = time.substring(0, 4);
        for (int i = 0; i < 13; i++) {
            oneYear.put(year + i, null);
        }
        return oneYear;
    }
}

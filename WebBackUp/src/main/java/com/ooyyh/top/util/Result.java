package com.ooyyh.top.util;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Map success(String code, String msg, Object data){
        Map result = new HashMap();
        result.put("code",code);
        result.put("msg",msg);
        result.put("data",data);
        return result;
    }
    public static Map success(String code, String msg){
        Map result = new HashMap();
        result.put("code",code);
        result.put("msg",msg);
        result.put("data",null);
        return result;
    }
    public static Map success(String msg){
        Map result = new HashMap();
        result.put("code","200");
        result.put("msg",msg);
        result.put("data",null);
        return result;
    }
    public  static Map success(){
        Map result = new HashMap();
        result.put("code","200");
        result.put("msg","success");
        result.put("data",null);
        return result;
    }
    public static Map error(String code, String msg){
        Map result = new HashMap();
        result.put("code",code);
        result.put("msg",msg);
        result.put("data",null);
        return result;
    }
    public static Map error(String msg){
        Map result = new HashMap();
        result.put("code","500");
        result.put("msg",msg);
        result.put("data",null);
        return result;
    }
    public static Map error(){
        Map result = new HashMap();
        result.put("code","500");
        result.put("msg","error");
        result.put("data",null);
        return result;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

}

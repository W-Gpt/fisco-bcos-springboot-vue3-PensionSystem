package com.ooyyh.top.controller;

import com.ooyyh.top.service.PensionService;
import com.ooyyh.top.util.ColorFul;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/pension")
public class PensionController {
    @Autowired
    PensionService pensionService;
//    @GetMapping("/getPensionInfo")
//    @ResponseBody
//    public Map getPensionInfo(@RequestHeader String token) throws UnsupportedEncodingException {
//        return pensionService.getPensionInfo(token);
//    }
//    @GetMapping("/getAllPayMent")
//    @ResponseBody
//    public Map getAllPayMent(@RequestHeader String token) throws UnsupportedEncodingException {
//        return pensionService.getAllPayMent(token);
//    }
//    @GetMapping("/getLaborInfo")
//    @ResponseBody
//    public Map getLaborInfo(@RequestHeader String token) throws UnsupportedEncodingException {
//        return pensionService.getLaborIndexPer(token);
//
//    }
    @GetMapping("/getPensionInfo")
    @ResponseBody
    public Map getPensionInfo(@RequestHeader String token) throws UnsupportedEncodingException {
        Map result = pensionService.getPensionInfo(token);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

    @GetMapping("/getAllPayMent")
    @ResponseBody
    public Map getAllPayMent(@RequestHeader String token) throws UnsupportedEncodingException {
        Map result = pensionService.getAllPayMent(token);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

    @GetMapping("/getLaborInfo")
    @ResponseBody
    public Map getLaborInfo(@RequestHeader String token) throws UnsupportedEncodingException {
        Map result = pensionService.getLaborIndexPer(token);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

}

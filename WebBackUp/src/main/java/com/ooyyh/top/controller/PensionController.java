package com.ooyyh.top.controller;

import com.ooyyh.top.service.PensionService;
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
    @GetMapping("/getPensionInfo")
    @ResponseBody
    public Map getPensionInfo(@RequestHeader String token) throws UnsupportedEncodingException {
        return pensionService.getPensionInfo(token);
    }
    @GetMapping("/getAllPayMent")
    @ResponseBody
    public Map getAllPayMent(@RequestHeader String token) throws UnsupportedEncodingException {
        return pensionService.getAllPayMent(token);
    }
    @GetMapping("/getLaborInfo")
    @ResponseBody
    public Map getLaborInfo(@RequestHeader String token) throws UnsupportedEncodingException {
        return pensionService.getLaborIndexPer(token);

    }
}

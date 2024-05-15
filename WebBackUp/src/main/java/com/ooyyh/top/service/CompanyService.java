package com.ooyyh.top.service;

import com.ooyyh.top.entity.Company;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Service
public class CompanyService {
    public Map addLaborInfo(String token, Company company) throws UnsupportedEncodingException {
        String tokenT = URLDecoder.decode(token, "UTF-8");
        return null;
    }
}

package com.mada.controller;

import com.mada.exceptionHandler.myExceptionHandler;
import com.mada.pojo.user;
import com.mada.response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import utils.commonUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")

public class login {
    @Autowired
    com.mada.service.userService userService;

    @ResponseBody
    @RequestMapping("getOpenId")
    public response getOpenId(String code) throws IOException {
        response r = new response();
        CloseableHttpResponse response = commonUtils.sendGet("https://api.weixin.qq.com/sns/jscode2session?appid=wx4bebe63d04ef4fcb&secret=6d9eefc218e3523f0a80f958af94f5d7&js_code=" + code + "&grant_type=authorization_code", null);
        String doc = EntityUtils.toString(response.getEntity(), "UTF-8");
        doc = doc.replaceAll("\\}", "");
        doc = doc.replaceAll("\\{", "");
        doc = doc.replaceAll("\"", "");
        System.out.println(doc);
        String[] datas = doc.split(",");
        Map<String, String> nameAndValue = new HashMap<>();
        for (String data : datas) {
            String[] kv = data.split(":");
            nameAndValue.put(kv[0], kv[1]);
        }
        user userEntity = userService.getUser(nameAndValue.get("openid"));
        if (userEntity == null)
            r.addAttribute("isLogin", false);
        else
        {
            r.addAttribute("isLogin", true);
            r.addAttribute("userEntity", userEntity);
        }
        r.addAttribute("code", code);
        r.setResponseSuccess();
        r.addAttribute("api", nameAndValue);
        return r;
    }

    @ResponseBody
    @RequestMapping("registered")
    public response registered(user userEntity) throws IOException {
        response r = new response();
        System.out.println(userEntity);
        Date now=new Date();
        userEntity.setBirth(now);
        userService.addUser(userEntity);
        r.setResponseSuccess();
        r.addAttribute("user", userEntity);
        return r;
    }


}

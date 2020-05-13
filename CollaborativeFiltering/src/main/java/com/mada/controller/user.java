package com.mada.controller;

import com.mada.message;
import com.mada.pojo.readingRecord;
import com.mada.response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import utils.commonUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")

public class user {
    @Autowired
    com.mada.service.userService userService;
    @Autowired
    ServletContext context;
    @ResponseBody
    @RequestMapping("getReadingRecordList")
    public response getReadingRecordList(String uId)  {
        response r = new response();

        List<readingRecord> readingRecordList = userService.getReadingRecordList(uId);

        r.addAttribute("readingRecordList", readingRecordList);
        r.setResponseSuccess();

        return r;
    }
    @ResponseBody
    @RequestMapping("getReadingBookList")
    public response getReadingBookList(String uId)  {
        response r = new response();

        List<com.mada.pojo.book> readingBookList = userService.getReadingBookList(uId);

        r.addAttribute("readingBookList", readingBookList);
        r.setResponseSuccess();

        return r;
    }
    @ResponseBody
    @RequestMapping("addReadingRecord")
    public response addReadingRecord(String uId, int bId, int score) throws IOException {
        response r = new response();
        System.out.println("uId" + uId);
        userService.addReadingRecord(uId, bId, score);

        r.setResponseSuccess();

        return r;
    }

    @ResponseBody
    @RequestMapping("registered")
    public response registered(com.mada.pojo.user userEntity) throws IOException {
        response r = new response();
        System.out.println(userEntity);
        Date now = new Date();
        userEntity.setBirth(now);
        userService.addUser(userEntity);
        r.setResponseSuccess();
        r.addAttribute("user", userEntity);
        return r;
    }

    @ResponseBody
    @RequestMapping("updateUserLebels")
    public response updateUserLebels(String uId, String[] lebels) throws IOException {
        response r = new response();
        String lebelsString = "";
        if (lebels != null) {
            for (int i = 0; i < lebels.length - 1; i++) {
                lebelsString += lebels[i] + ",";

            }
            lebelsString += lebels[lebels.length - 1];
        }
        if (userService.updateUserLebels(uId, lebelsString) >= 1) {
            r.setResponseSuccess();
        } else {
            r.setResponseFail();
        }
//        r.addAttribute("user", userEntity);
        return r;
    }

    @ResponseBody
    @RequestMapping("getMessage")
    public response getMessage() throws IOException {
        response r = new response();
        r.setResponseSuccess();
        r.addAttribute("message", context.getAttribute("message"));
        return r;
    }
    @ResponseBody
    @RequestMapping(value="setMessage",produces="text/html;charset=UTF-8")
    public String setMessage(String name,String password,String message) throws IOException {

        System.out.println("name:"+name);
        System.out.println("password:"+password);
        System.out.println("message:"+message);
       if(name.equals("admin")&&password.equals("123456"))
       {
           message ms=new message();
           ms.setMessageDate(new Date());
           ms.setMessage(message);
           context.setAttribute("message",ms);
           return "设置通知成功！";
       }
       else
       {
           return "用户名或者密码错误，设置通知失败！";

       }

    }
}

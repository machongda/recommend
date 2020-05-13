package com.mada.controller;

import com.mada.pojo.readingRecord;
import com.mada.response;
import com.mada.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/book")

public class book {
    @Autowired
    com.mada.service.bookService bookService;

    @ResponseBody
    @RequestMapping("searchBook")
    public response searchBook(String search) throws IOException {
        response r = new response();
        List<com.mada.pojo.book> searchBookResult= bookService.searchBook(search);
        r.addAttribute("searchBookResult", searchBookResult);
        r.setResponseSuccess();

        return r;
    }

    @ResponseBody
    @RequestMapping("getRecommendBook")
    public response getRecommendBook(String[] lebels) throws IOException {
        List<com.mada.pojo.book> recommendBookResult;
        response r = new response();

        int count=bookService.getRecommendBookCount(lebels);
        System.out.println("count:"+count);
        if(count<100){
            recommendBookResult= bookService.getRecommendBook(lebels,0);
        }
        else{
             double d;
             int start;
            while(true){
                d = Math.random();
                start=(int)(d*20);
                if(start*100<=count-100)
                    break;
            }
            System.out.println("start:"+start);
            recommendBookResult= bookService.getRecommendBook(lebels,start*100);
        }
        r.addAttribute("recommendBookResult", recommendBookResult);
        r.setResponseSuccess();

        return r;
    }
    @ResponseBody
    @RequestMapping("getComment")
    public response getComment(int bId) throws IOException {
        response r = new response();
        List<com.mada.pojo.comment> comment= bookService.getComment(bId);
        r.addAttribute("comment", comment);
        r.setResponseSuccess();
        return r;
    }

    @ResponseBody
    @RequestMapping("getBook")
    public response getBook(int bId) throws IOException {
        response r = new response();
        com.mada.pojo.book book= bookService.getBook(bId);
        r.addAttribute("book", book);
        r.setResponseSuccess();
        return r;
    }

    @ResponseBody
    @RequestMapping("getRecommendBookCount")
    public String getRecommendBookCount(String[] lebels) throws IOException {
        int count=bookService.getRecommendBookCount(lebels);
        return String.valueOf(count);
    }

}

package com.mada.controller;


import com.mada.pojo.book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
public class test {
    @Autowired
    com.mada.service.bookService bookService;

    @RequestMapping("/test")
    public ModelAndView test(int bId) {

        ModelAndView mav = new ModelAndView();
        book book = bookService.getBook(bId);
        // 放入jsp路径
        mav.setViewName("test");
        mav.addObject("book",book);
        return mav;
    }


}

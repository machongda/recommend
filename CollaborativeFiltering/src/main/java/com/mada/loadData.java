package com.mada;
/*
* 初始化时加载数据到内存，即用到的用户和他们的阅读记录
*
* */

import com.mada.pojo.book;
import com.mada.pojo.user;
import com.mada.service.bookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;


import javax.servlet.ServletContext;
import java.util.Date;

public class loadData implements ServletContextAware {
    @Autowired
    com.mada.service.userService userService;

    @Override
    public void setServletContext(ServletContext servletContext) {

        System.out.println("开始加载数据到内存");
        message ms=new message();
        Date date=new Date();
        ms.setMessageDate(date);
        ms.setMessage("欢迎使用协同过滤图书推荐系统！");
        user user = userService.getUser("102601213");
        System.out.println("用户"+user.getUId()+"获取成功");
        System.out.println("阅读数量："+user.getReadingRecordList().size());
//        System.out.println("路径1："+this.getClass().getResource(""));
//        System.out.println("路径2："+this.getClass().getResource("/"));
        servletContext.setAttribute("message",ms);

    }
}

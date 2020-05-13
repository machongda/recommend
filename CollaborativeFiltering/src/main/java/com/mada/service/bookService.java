package com.mada.service;

import com.mada.mapper.bookMapper;
import com.mada.pojo.book;
import com.mada.pojo.comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class bookService {
    @Autowired
    public bookMapper bookMapper;


    public List<book> searchBook(String search){
        return bookMapper.searchBook(search);
    }

    public List<book> getRecommendBook(String[] lebels,int start){
        return bookMapper.getRecommendBook(lebels,start);
    }
    public int getRecommendBookCount(String[] lebels){
        return bookMapper.getRecommendBookCount(lebels);
    }
    public List<comment> getComment( int bId){
        return bookMapper.getComment(bId);
    };
    public book getBook( int bId){
        return bookMapper.getBook(bId);
    };
}
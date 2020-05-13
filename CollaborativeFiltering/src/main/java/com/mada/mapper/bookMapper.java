package com.mada.mapper;

import com.mada.pojo.book;
import com.mada.pojo.comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface bookMapper {

    public List<book> searchBook(String search);
    public List<book> getRecommendBook(@Param("array") String[] lebels,@Param("start") int start);
    public int getRecommendBookCount(@Param("array") String[] lebels);
    public List<comment> getComment(@Param("bId") int bId);
    public book getBook(@Param("bId") int bId);
}

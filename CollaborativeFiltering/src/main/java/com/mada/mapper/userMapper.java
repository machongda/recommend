package com.mada.mapper;

import com.mada.pojo.book;
import com.mada.pojo.readingRecord;
import com.mada.pojo.user;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface userMapper {
    public user getUser(String uId);
    public List<user> getUserList();
    public void addUser(user userEntity);
    public List<readingRecord> getReadingRecordList(String uId);
    public void addReadingRecord(@Param("uId") String uId, @Param("bId") int bId, @Param("score") int score);
    public int updateUserLebels(@Param("uId") String uId,@Param("lebels") String lebels);
    public List<book> getReadingBookList(@Param("uId") String uId);
}

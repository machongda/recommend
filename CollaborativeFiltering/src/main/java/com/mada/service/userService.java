package com.mada.service;

import com.mada.mapper.bookMapper;
import com.mada.mapper.userMapper;
import com.mada.pojo.book;
import com.mada.pojo.readingRecord;
import com.mada.pojo.user;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userService {
    @Autowired
    public userMapper userMapper;

    public user getUser(String uId) {
        return userMapper.getUser(uId);
    }

    public void addUser(user userEntity) {
        userMapper.addUser(userEntity);
    }

    public List<user> getUserList(user userEntity) {
        return userMapper.getUserList();
    }

    public List<readingRecord> getReadingRecordList(String uId) {
        return userMapper.getReadingRecordList(uId);
    }

    public void addReadingRecord(String uId, int bId, int score) { userMapper.addReadingRecord(uId, bId, score); }
    public int updateUserLebels(String uId, String lebels){
        return  userMapper.updateUserLebels(uId,lebels);
    };
    public List<book> getReadingBookList(String uId){
        return  userMapper.getReadingBookList(uId);
    };

}

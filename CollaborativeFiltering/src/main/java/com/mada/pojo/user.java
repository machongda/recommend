package com.mada.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class user {
    private  String uId;
    private String imagePath;
    private String nickname;
    private String sex;
    private Date birth;
    private String password;
    private String lebels;
    List<readingRecord> readingRecordList;

}

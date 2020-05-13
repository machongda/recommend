package com.mada.pojo;

import lombok.Data;

import java.util.List;


@Data
public class book {
    private  int bId;
    private String isbn;
    private String name;
    private String coverPath;
    private String authorName;
    private String publishingHouse;
    private String publishingYear;
    private String labels;
    private String briefIntroduction;
    private float score;
    private  int pointNumber;
    private List<String> authorUrlList;
    private List<String> recommonedUrlList;
}

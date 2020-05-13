package com.mada.pojo;

import lombok.Data;

@Data
public class comment {
    private int cId;
    private int bId;
    private String nickname;
    private String content;

}

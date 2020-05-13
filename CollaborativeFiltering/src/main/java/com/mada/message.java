package com.mada;

import lombok.Data;

import java.util.Date;
@Data
public class message {
   private Date messageDate;
    private String content;
    public void setMessage(String content) {
        this.content = content;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}

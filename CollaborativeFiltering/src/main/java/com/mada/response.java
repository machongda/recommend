package com.mada;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class response {
   private String status;
    private String errMsg;
    private Map<String,Object> data;
    public response(){
        this.status="fail";
        this. errMsg="";
        this.data=new HashMap<String,Object>();
    }

    public void setResponseSuccess(){
        this.status="success";
    }
    public void setResponseFail(){
        this.status="fail";
    }
    public void setErrMsg(String errMsg){
        this.errMsg=errMsg;
    }
    public void addAttribute(String name,Object data){
        this.data.put(name,data);
    }
}

package com.fisco.app.entity;


import java.util.List;

public class ResponseListData<T>{
    private int code;
    private String msg;
    private List<T> data;

    public static ResponseListData success( String msg) {
        ResponseListData responseListData = new ResponseListData();
        responseListData.setCode(0);
        responseListData.setMsg(msg);
        return responseListData;
    }

    public static <T> ResponseListData<T> success( List<T> t) {
        ResponseListData responseListData = new ResponseListData();
        responseListData.setCode(0);
        responseListData.setData(t);
        return responseListData;
    }

    public static ResponseListData error(String msg) {
        ResponseListData responseListData = new ResponseListData();
        responseListData.setCode(-1);
        responseListData.setMsg(msg);
        return responseListData;
    }

    public static <T> ResponseListData<T> error( List<T> t) {
        ResponseListData responseListData = new ResponseListData();
        responseListData.setCode(-1);
        responseListData.setData(t);
        return responseListData;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

package com.fisco.app.entity;

/**
 * @Date  2021/5/25
 * @Description 返回前端的结果集
 * @author Lyontang
**/
public class ResponseData<T> {

    private int code;
    private String msg;
    private T data;

    public static ResponseData success( String msg) {
        ResponseData responseData = new ResponseData();
        responseData.setCode(0);
        responseData.setMsg(msg);
        return responseData;
    }

    public static <T> ResponseData<T> success( T t) {
        ResponseData responseData = new ResponseData();
        responseData.setCode(0);
        responseData.setData(t);
        return responseData;
    }

    public static ResponseData error(String msg) {
        ResponseData responseData = new ResponseData();
        responseData.setCode(-1);
        responseData.setMsg(msg);
        return responseData;
    }

    public static <T> ResponseData<T> error( T t) {
        ResponseData responseData = new ResponseData();
        responseData.setCode(-1);
        responseData.setData(t);
        return responseData;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

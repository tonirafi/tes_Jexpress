package com.template.project.http.bean;

/**
 * Created by 5Mall<zhangwei> on 2018/7/16
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class BaseResponse2<T> {

    protected int code;
    protected String msg;
    protected T data;

    public boolean isSuccess() {
        return code == 0;
    }

    public boolean isResponseSuccessful() {
        return isSuccess() && hasData();
    }

    public boolean hasData() {
        return data != null;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getLocalMsg() {
        if (!hasData())
            return "the data in BaseResponse is null";
        return getMsg();
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}

package com.template.project.exception;


/**
 * 定义
 */
public class ApiException extends RuntimeException {

    private int code;
    public String msg;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
package com.template.project.exception;

/**
 *  接口正常请求后 服务器返回的异常
 */
public class ServerException extends RuntimeException {

    private int code;

    public ServerException(String detailMessage) {
        super(detailMessage);
        this.code = 200;
    }

    public ServerException(String detailMessage, int code) {
        super(detailMessage);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
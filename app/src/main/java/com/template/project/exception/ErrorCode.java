package com.template.project.exception;

public interface ErrorCode {
    /**
     * 未知错误
     */
    int UNKNOWN = 1000;
    /**
     * 解析错误
     */
    int PARSE_ERROR = 1001;
    /**
     * http请求/协议错误
     */
    int HTTP_ERROR = 1003;
    /**
     * 网络错误
     */
    int NETWORK_ERROR = 500;
    /**
     * 服务器返回错误
     */
    int SERVER_ERROR = 200;
    /**
     * 服务器响应超时错误
     */
    int SERVER_TIME_OUT_ERROR = 201;

    /**
     * 用户数据加载异常
     */
    int IllEGAL_USER_ERROR = 1004;

    /**
     * 签名验证错误
     */
    int AUTH_ERROR = 401;

}

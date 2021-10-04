package com.template.project.http.bean;


import com.google.gson.annotations.SerializedName;

/**
 * Created by 5Mall<zhangwei> on 2018/7/17
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class QSCToken {
    /**
     * 用于校验 两个小时过期，过期后可以通过refreshToken 刷新
     */
    @SerializedName("accessToken")
    public String accessToken;

    /**
     * token的类型 暂时无用，保留字段
     */
    @SerializedName("tokenType")
    public String tokenType;

    /**
     * 30 天过期
     */
    @SerializedName("refreshToken")
    public String refreshToken;

    /**
     * 过期时间(e.g 86400)
     */
    @SerializedName("expiresIn")
    public long expiresIn;

    @SerializedName("tokenExpires")
    public long tokenExpires;

    /**
     * 服务器创建token时的时间戳，可以用来比较两个token的新旧
     */
    @SerializedName("srv_create_time")
    public long serverTimestamp;

    @SerializedName("signature")
    public String signature;

    @SerializedName("uid")
    public String userId;

    @SerializedName("newUserFlag")
    public String newUserFlag;
    /**
     * 刷新token过期时间判断 30天过期客户端得重新登陆 //30天过期客户端得重新登陆
     */
    @SerializedName("refreshTokenExpires")
    public long refreshTokenExpires;

    public boolean isRefreshTokenExpired() {
        return covertExpiresToTimestamp(refreshTokenExpires) <= System.currentTimeMillis();
    }

    public long covertExpiresToTimestamp(long expires) {
        return expires * 1000 + serverTimestamp;
    }

    public boolean isExpired() {
//        Date().after(Date(token.covertExpiresToTimestamp()))
        return covertExpiresToTimestamp(expiresIn) <= System.currentTimeMillis();
    }

    public void initExpiresTimeIfEmpty() {
        if (this.serverTimestamp <= 0) { //当没有返回服务器时间戳的时候 以设备当前时间进行赋值 用于接口请求 token是否过期的判断
            this.serverTimestamp = System.currentTimeMillis();
        }

        if (this.expiresIn <= 0) { //当没有返回expiresIn数据的时候 默认给定7200s过期时间
            this.expiresIn = 7200;
        }

        if (this.refreshTokenExpires <= 0) { //当没有返回refreshTokenExpires数据的时候 默认给定30天过期时间 到期客户端需要重新登陆
            this.refreshTokenExpires = 30 * 24 * 60 * 60;
        }

    }
}

package com.template.project.http.bean

import com.google.gson.annotations.SerializedName

class QSCToken {
    @SerializedName("accessToken")
    var accessToken: String? = null

    @SerializedName("tokenType")
    var tokenType: String? = null

    @SerializedName("refreshToken")
    var refreshToken: String? = null

    @SerializedName("expiresIn")
    var expiresIn: Long = 0

    @SerializedName("tokenExpires")
    var tokenExpires: Long = 0

    @SerializedName("srv_create_time")
    var serverTimestamp: Long = 0

    @SerializedName("signature")
    var signature: String? = null

    @SerializedName("uid")
    var userId: String? = null

    @SerializedName("newUserFlag")
    var newUserFlag: String? = null

    @SerializedName("refreshTokenExpires")
    var refreshTokenExpires: Long = 0
    val isRefreshTokenExpired: Boolean
        get() = covertExpiresToTimestamp(refreshTokenExpires) <= System.currentTimeMillis()

    fun covertExpiresToTimestamp(expires: Long): Long {
        return expires * 1000 + serverTimestamp
    }

    //        Date().after(Date(token.covertExpiresToTimestamp()))
    val isExpired: Boolean
        get() =//        Date().after(Date(token.covertExpiresToTimestamp()))
            covertExpiresToTimestamp(expiresIn) <= System.currentTimeMillis()

    fun initExpiresTimeIfEmpty() {
        if (serverTimestamp <= 0) { //当没有返回服务器时间戳的时候 以设备当前时间进行赋值 用于接口请求 token是否过期的判断
            serverTimestamp = System.currentTimeMillis()
        }
        if (expiresIn <= 0) { //当没有返回expiresIn数据的时候 默认给定7200s过期时间
            expiresIn = 7200
        }
        if (refreshTokenExpires <= 0) { //当没有返回refreshTokenExpires数据的时候 默认给定30天过期时间 到期客户端需要重新登陆
            refreshTokenExpires = 30 * 24 * 60 * 60.toLong()
        }
    }
}
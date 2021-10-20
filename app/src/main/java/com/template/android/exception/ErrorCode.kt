package com.template.android.exception

interface ErrorCode {
    companion object {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001

        /**
         * http请求/协议错误
         */
        const val HTTP_ERROR = 1003

        /**
         * 网络错误
         */
        const val NETWORK_ERROR = 500

        /**
         * 服务器返回错误
         */
        const val SERVER_ERROR = 200

        /**
         * 服务器响应超时错误
         */
        const val SERVER_TIME_OUT_ERROR = 201

        /**
         * 用户数据加载异常
         */
        const val IllEGAL_USER_ERROR = 1004

        /**
         * 签名验证错误
         */
        const val AUTH_ERROR = 401
    }
}
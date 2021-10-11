package com.tes.frezzmart.exception

/**
 * 接口正常请求后 服务器返回的异常
 */
class ServerException : RuntimeException {
    var code: Int
        private set

    constructor(detailMessage: String?) : super(detailMessage) {
        code = 200
    }

    constructor(detailMessage: String?, code: Int) : super(detailMessage) {
        this.code = code
    }

}
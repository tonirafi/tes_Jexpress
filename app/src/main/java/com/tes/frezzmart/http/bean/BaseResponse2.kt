package com.tes.frezzmart.http.bean

/**
 * Created by 5Mall<zhangwei> on 2018/7/16
 * Email:zhangwei@qingsongchou.com
 * 描述：
</zhangwei> */
open class BaseResponse2<T> {
    var code = 0
    var msg: String? = null
    var data: T? = null
        private set
    val isSuccess: Boolean
        get() = code == 0

    val isResponseSuccessful: Boolean
        get() = isSuccess && hasData()

    fun hasData(): Boolean {
        return data != null
    }

    val localMsg: String?
        get() = if (!hasData()) "the data in BaseResponse is null" else msg

    fun setData(data: T) {
        this.data = data
    }
}
package com.template.android.utils.upload.image.bean

import android.text.TextUtils
import com.google.gson.annotations.SerializedName

/**
 *   Created by 5Mall<zhangwei> on 2018/8/13
 *   Email:zhangwei@qingsongchou.com
 *   描述：
 */
class CloudinaryConfig {

    /*
    "Timestamp": "1532414862",
    "ApiKey": "318368517681473",
    "Signature": "4c57bd0a83a6e0fbeede5ddda145f45f4e291a8b",
    "CloudName": "dqgl4hkkx"
    */
    @SerializedName("Timestamp")
    var timestamp: String? = null

    @SerializedName("ApiKey")
    var apiKey: String? = null

    @SerializedName("Signature")
    var signature: String? = null

    @SerializedName("CloudName")
    var cloudName: String? = null

    fun getTimestamp(): Long {

        if (!TextUtils.isEmpty(timestamp) && TextUtils.isDigitsOnly(timestamp)) {
            return timestamp!!.toLong()
        }

        return 0
    }
}
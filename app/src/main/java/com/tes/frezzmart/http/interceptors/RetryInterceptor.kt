package com.tes.frezzmart.http.interceptors

import com.tes.frezzmart.utils.LogUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(//最大重试次数
        var maxRetry: Int) : Interceptor {
    private var retryNum = 0 //假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        LogUtil.logI("Interceptor", "RetryInterceptor num:$retryNum")
        while (!response.isSuccessful && retryNum < maxRetry) {
            retryNum++
            LogUtil.logI("Interceptor", "RetryInterceptor num:$retryNum")
            response = chain.proceed(request)
        }
        return response
    }

}
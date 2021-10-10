package com.template.project.http.interceptors

import com.template.project.MyApplication.Companion.getContext
import com.template.project.utils.LogUtil
import com.template.project.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class OfflineCacheInterceptor : Interceptor {
    private var maxCacheTimeSecond = 7 * 24 * 3600

    constructor() {}
    constructor(maxCacheTimeSecond: Int) {
        this.maxCacheTimeSecond = maxCacheTimeSecond
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetworkUtils.isConnected()) { //无网络的时候 检查maxCacheTimeSecond秒内的缓存，包括过期的缓存
            LogUtil.logI("Interceptor", "OfflineCacheInterceptor url=" + request.url())
            LogUtil.logI("Interceptor", "OfflineCacheInterceptor cacheControl=" + request.cacheControl().toString())
            request = chain.request().newBuilder()
                    .cacheControl(CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(maxCacheTimeSecond, TimeUnit.SECONDS)
                            .build())
                    .build()
            getContext().mainThreadToast("Oops,Can not access the internet.")

//            Response response = chain.proceed(request);
//            if (response.cacheResponse() == null) {
//                return response.newBuilder()
//                        .build();
//            }
//
//            return response;
        }
        return chain.proceed(request)
    }
}
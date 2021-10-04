package com.template.project.http.interceptors;


import android.text.TextUtils;

import androidx.annotation.NonNull;


import com.template.project.utils.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class OnlineCacheInterceptor implements Interceptor {


    private int maxCacheTimeSecond;

    public OnlineCacheInterceptor(int maxCacheTimeSecond) {
        this.maxCacheTimeSecond = maxCacheTimeSecond;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = chain.proceed(request);

        if (request.method().equalsIgnoreCase("GET")) { //仅针对GET请求 作自定义cacheControl策略 因为okhttp 默认也仅针对GET请求作缓存
            final String cacheControl = originalResponse.header("Cache-Control");
            LogUtil.logI("Interceptor", "OnlineCacheInterceptor Response cacheControl=" + cacheControl);
            if (TextUtils.isEmpty(cacheControl)) { //如果服务端没有缓存机制 则使用本地自定义的缓存策略 该缓存会在接口再次请求且未指定cacheControl时 被用作新请求的 Request cacheControl
                LogUtil.logI("Interceptor", "OnlineCacheInterceptor 使用本地自定义的Response cacheControl");
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", "max-age=" + maxCacheTimeSecond)
                        .build();
            }

            //服务端如有cacheControl 则使用服务端返回的cacheControl 同样在接口再次请求且未指定cacheControl时 被用作新请求的 Request cacheControl
        }

        return originalResponse;
    }
}

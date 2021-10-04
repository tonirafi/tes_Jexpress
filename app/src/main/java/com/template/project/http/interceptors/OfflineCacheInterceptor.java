package com.template.project.http.interceptors;


import androidx.annotation.NonNull;

import com.template.project.MyApplication;
import com.template.project.utils.LogUtil;
import com.template.project.utils.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 设置没有网络的情况下，
 * 的缓存时间
 * 通过：addInterceptor 设置
 */
public class OfflineCacheInterceptor implements Interceptor {


    private int maxCacheTimeSecond = 7 * 24 * 3600;

    public OfflineCacheInterceptor() {
    }

    public OfflineCacheInterceptor(int maxCacheTimeSecond) {
        this.maxCacheTimeSecond = maxCacheTimeSecond;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isConnected()) { //无网络的时候 检查maxCacheTimeSecond秒内的缓存，包括过期的缓存
            LogUtil.logI("Interceptor", "OfflineCacheInterceptor url=" + request.url());
            LogUtil.logI("Interceptor", "OfflineCacheInterceptor cacheControl=" + request.cacheControl().toString());
            request = chain.request().newBuilder()
                    .cacheControl(new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(maxCacheTimeSecond, TimeUnit.SECONDS)
                            .build())
                    .build();

            MyApplication.getContext().mainThreadToast("Oops,Can not access the internet.");

//            Response response = chain.proceed(request);
//            if (response.cacheResponse() == null) {
//                return response.newBuilder()
//                        .build();
//            }
//
//            return response;
        }

        return chain.proceed(request);
    }
}

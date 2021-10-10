package com.template.project.http.api

import com.google.gson.GsonBuilder
import com.template.project.BuildConfig
import com.template.project.MyApplication.Companion.getContext
import com.template.project.utils.AppUtilNew.Companion.isNetworkAvailable
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RestAdapter() {

    var onlineInterceptor: Interceptor = Interceptor { chain ->
        val response: Response = chain.proceed(chain.request())
        val maxAge =
            60 // read from cache for 60 seconds even if there is internet connection
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }

    var offlineInterceptor = Interceptor { chain ->
        var request: Request = chain.request()
        if (!isNetworkAvailable(getContext())) {
            val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
        chain.proceed(request)
    }



    fun callApi(): ApiService? {
        val retrofit: Retrofit
        try {

            val gson = GsonBuilder()
                .create()


            var cacheSize = 10 * 1024 * 1024 // 10 MB

            var cache: Cache = Cache(getContext().cacheDir, cacheSize.toLong())

            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(offlineInterceptor)
                .addNetworkInterceptor(onlineInterceptor)
                .connectTimeout(REQ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .cache(cache)
                .build()



            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.URL_DOMAIN)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }


    companion object {
        private val REQ_TIMEOUT = 90
    }
}




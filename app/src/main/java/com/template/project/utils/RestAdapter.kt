package com.template.project.utils

import com.google.gson.GsonBuilder
import com.template.project.BuildConfig
import com.template.project.http.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit


class RestAdapter() {


    fun callApi(): ApiService? {
        val retrofit: Retrofit
        try {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val gson = GsonBuilder()
//                .excludeFieldsWithoutExposeAnnotation()
                .create()

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .connectTimeout(REQ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQ_TIMEOUT.toLong(), TimeUnit.SECONDS)
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


    fun callApi(baseUrl: String): ApiService? {

        val retrofit: Retrofit
        try {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(REQ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
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




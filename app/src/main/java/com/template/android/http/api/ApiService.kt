package com.template.android.http.api

import com.template.android.BuildConfig
import com.template.android.http.bean.NewsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET(BuildConfig.URL_DOMAIN + "v2/everything")
    fun listNews(
        @Query("q") search: String,
        @Query("page") page: Int,
        @Query("pageSize") limit: Int,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") accessToken: String,
        @Header("Cache-Control") cache_control: String?
    ): Observable<NewsResponse>

}
package com.tes.frezzmart.http.api

import com.tes.frezzmart.BuildConfig
import com.tes.frezzmart.http.bean.NewsResponse
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET(BuildConfig.URL_DOMAIN + "v2/everything")
    fun listNews(@Query("q") search:String, @Query("apiKey") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<NewsResponse>

}
package com.template.android.http.api

import com.template.android.BuildConfig
import com.template.android.http.bean.Warung
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET(BuildConfig.URL_DOMAIN + "mobilewarung.json")
    fun getWarung(
        @Header("Cache-Control") cache_control: String?
    ): Observable<Warung>

}
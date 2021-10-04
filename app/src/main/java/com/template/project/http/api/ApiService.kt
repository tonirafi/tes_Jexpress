package com.template.project.http.api

import com.google.gson.JsonObject
import com.template.project.BuildConfig
import com.template.project.http.bean.BaseBean
import com.template.project.http.bean.BaseResponse
import com.template.project.http.bean.User
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "user/login")
    fun login(@Body data: JsonObject): Observable<BaseResponse<User>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "home/list-banner")
    fun listBanner(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "home/list-iklan")
    fun listIklanView(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "home/saldo-view")
    fun saldoView(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "home/view-reward")
    fun transactionRewad(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "iklan/list-iklan")
    fun listIklan(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "iklan/saldo-total-iklan")
    fun saldoTotalIklan(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "iklan/list-duration")
    fun listDuration(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "iklan/list-type-iklan")
    fun listTypeIklan(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "iklan/iklan-create")
    fun crateIklan(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "iklan/update-url-time")
    fun updateIklanUrlTime(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "iklan/iklan-update")
    fun updateIklanPay(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>


    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "home/app-config")
    fun appConfig(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<JsonObject>>


    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "topup/list-topup")
    fun listTopup(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "topup/list-biaya")
    fun listNominalTopup(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "topup/create-invoice")
    fun crateInvoice(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<JsonObject>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "penarikan/list-penarikan")
    fun listPenarikan(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "penarikan/list-akun-penarikan")
    fun listAkunPenarikan(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "penarikan/list-nominal")
    fun listNominalPenarikan(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "penarikan/tarik-uang")
    fun buatPenarikan(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "penarikan/update-tarik-uang")
    fun updatePenarikan(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>


    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "transaction/insert")
    fun insertTransaction(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "profile/detail-user")
    fun detailUser(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<JsonObject>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "profile/update-user")
    fun updateUser(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "profile/update-password")
    fun updatePassword(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>


    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "bank/list-bank")
    fun listMyBank(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "bank/list-code-bank")
    fun listCodeBank(@Body data: JsonObject,@Query("page") page:Int ,@Query("limit") limit:Int ,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<JsonObject>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "bank/create-bank")
    fun createAkunBank(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "user/signup")
    fun createAkunGV(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "bank/update-bank")
    fun updateAkunBank(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

    @Headers("Content-Type: application/json")
    @POST(BuildConfig.URL_DOMAIN + "user/forgot-password")
    fun forgotPassword(@Body data: JsonObject,@Query("access-token") accessToken:String, @Header("Cache-Control") cache_control: String?): Observable<BaseResponse<BaseBean>>

}
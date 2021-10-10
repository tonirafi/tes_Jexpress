package com.template.project.http.interceptors

import com.template.project.AppConstants
import com.template.project.BuildConfig
import com.template.project.http.bean.QSCToken
import com.template.project.utils.LogUtil
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by 5Mall<zhangwei> on 2018/7/20
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
class InjectHeaderInterceptor : Interceptor {
    /**
     * User-Agent: PeduliSehat/1.0 (Android5.0.1 API21)
     * Qsc-Peduli-Token:
     * Authorization: UID:Signature
     * ExpireIn:
     * Platform:Android
     * DeviceID:
     * DeviceLan: 设备语言 en zh in
     *
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(InjectHeaderInterceptor::class.java) {
            val builder = chain.request().newBuilder()
            addHeaders(builder)

//            Passport.instance.get()?.let {
//                syncRefreshToken(it)
//            }?.let {
//                addHeaders(builder, it, false)
//            }

            return chain.proceed(builder.build())
        }
    }

    private fun syncRefreshToken(token: QSCToken): QSCToken {
        if (token.isExpired) {
            LogUtil.logI("Interceptor", "InjectHeaderInterceptor refreshQSCToken...")
            val response = getTokenRefreshCall(token).execute()
            LogUtil.logI("Interceptor", "InjectHeaderInterceptor response.isSuccessful=${response.isSuccessful}")
//            try {
//                if (response.isSuccessful) {
//                    val responseJson = response.body?.string()
//                    LogUtil.logI("Interceptor", "InjectHeaderInterceptor response.body()=$responseJson")
//                    val data = Gson().fromJson(responseJson, object : TypeToken<BaseResponse<QSCToken>>() {}.type) as? BaseResponse<QSCToken>
//                    data?.takeIf { it.isResponseSuccessful }?.let {
//                        return it.data.apply {
//                            Passport.instance.save(this, true)
//                            LogUtil.logI("Interceptor", "InjectHeaderInterceptor refreshQSCToken new token=${this.accessToken}")
//                        }
//                    }
//                }
//
//            } catch (e: Exception) {
//                LogUtil.logI("Interceptor", "InjectHeaderInterceptor Exception=${e.message}")
//            } finally {
//                response.close()
//                response.body?.close()
//            }
        }

        return token
    }

    companion object {
        private fun addHeaders(builder: Request.Builder, token: QSCToken? = null, regularHeaders: Boolean = true) {
            if (regularHeaders) {
                //        builder.header("Cache-Control","public,max-age=20")
                builder.header(AppConstants.Http.HEADER_ACCEPT_KEY, AppConstants.Http.HEADER_ACCEPT_VALUE)
                builder.header(AppConstants.Http.HEADER_ACCEPT_LANGUAGE, AppConstants.Http.deviceLan)
                builder.header(AppConstants.Http.HEADER_PLATFORM, AppConstants.Http.HEADER_PLATFORM_VALUE)
                builder.header(AppConstants.Http.HEADER_USER_AGENT, AppConstants.Http.userAgent)
                builder.header(AppConstants.Http.HEADER_DEVICE_ID, AppConstants.Http.deviceId)
                builder.header(AppConstants.Http.HEADER_DEVICE_LAN, AppConstants.Http.deviceLan)
                builder.header(AppConstants.Http.HEADER_APP_VERSION, AppConstants.Http.appVersion)
            }

            token?.let {
                builder.header(AppConstants.Http.HEADER_ACCESS_TOKEN, it.accessToken)
                builder.header(AppConstants.Http.HEADER_AUTHORIZATION, "${it.userId}:${it.signature}")
                builder.header(AppConstants.Http.HEADER_EXPIRES_IN, it.expiresIn.toString())
                builder.header(AppConstants.Http.HEADER_TOKEN_EXPIRES, it.tokenExpires.toString())
            }

        }


        fun getTokenRefreshCall(token: QSCToken): Call {
            val client = OkHttpClient.Builder()
                .connectTimeout(AppConstants.Http.HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(AppConstants.Http.HTTP_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(AppConstants.Http.HTTP_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
                //默认重试一次，若需要重试N次，则要实现拦截器。
                .retryOnConnectionFailure(true)
                //请求重试
                .addInterceptor(RetryInterceptor(3))
                .also {
                    if (BuildConfig.DEBUG) {
                        it.addNetworkInterceptor(Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor").newInstance() as Interceptor)
                    }
                }
                .build()

            val request = Request.Builder()
                .url("v1/oauth2/token?grantType=refresh_token&refreshToken=${token.refreshToken}")
                .get().also {
                    addHeaders(it, token)
                }.build()
            return client.newCall(request)
        }
    }

}

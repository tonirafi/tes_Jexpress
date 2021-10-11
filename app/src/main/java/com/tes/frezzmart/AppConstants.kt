package com.tes.frezzmart

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import com.tes.frezzmart.MyApplication.Companion.getContext
import com.tes.frezzmart.utils.LocaleUtil

object AppConstants {
    const val MARKET_URI = "market://details?id="
    const val GOOGLE_PLAY_MARKET_URI_PREFIX = "https://play.google.com/store/apps/"
    const val TOKEN_SERVER = "token_server"
    const val USER_LOGIN = "user_login"
    const val USER_ID = "user_id"

    object ResponeCode {
        const val SUCCESED = "200"
    }

    class CardId
    object RequestCode {
        const val DEFAULT_RSC_CODE = 1000
        const val RSC_LOGIN = 3003
    }

    object Http {
        const val DEFAULT_REQ_CACHE_TIME = 60 //默认在线接口请求的缓存时间为60秒
        const val HTTP_CONNECTION_TIMEOUT = 15
        const val HTTP_READ_TIMEOUT = 0
        const val HTTP_WRITE_TIMEOUT = 15
        const val HEADER_ACCEPT_KEY = "Accept"
        const val HEADER_ACCEPT_VALUE = "application/json"
        const val HEADER_ACCEPT_LANGUAGE = "Accept-Language"
        const val HEADER_USER_AGENT = "User-Agent"
        const val HEADER_ACCESS_TOKEN = "App-Token"
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HEADER_EXPIRES_IN = "ExpiresIn"
        const val HEADER_TOKEN_EXPIRES = "TokenExpires"
        const val HEADER_PLATFORM = "Platform"
        const val HEADER_PLATFORM_VALUE = "Android"
        const val HEADER_DEVICE_ID = "DeviceID"
        const val HEADER_DEVICE_LAN = "DeviceLan"
        const val HEADER_APP_VERSION = "Version"

        @get:SuppressLint("HardwareIds")
        val deviceId: String
            get() = Settings.Secure.getString(getContext().contentResolver, Settings.Secure.ANDROID_ID)

        val userAgent: String
            get() = String.format("App/%s (%s%s API%s)", BuildConfig.VERSION_NAME, HEADER_PLATFORM_VALUE, Build.VERSION.RELEASE, Build.VERSION.SDK_INT)

        val deviceLan: String
            get() = LocaleUtil.getLanguage(getContext())

        val appVersion: String
            get() = BuildConfig.VERSION_NAME
    }
}
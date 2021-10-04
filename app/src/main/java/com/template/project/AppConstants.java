package com.template.project;

import android.annotation.SuppressLint;
import android.provider.Settings;


import com.template.project.utils.LocaleUtil;

public class AppConstants {

    public final static String MARKET_URI = "market://details?id=";
    public final static String GOOGLE_PLAY_MARKET_URI_PREFIX = "https://play.google.com/store/apps/";
    public final static String TOKEN_SERVER = "token_server";
    public final static String USER_LOGIN = "user_login";
    public final static String USER_ID = "user_id";



    public static class ResponeCode {
        public final static String SUCCESED = "200";
    }



    public static class CardId {

        /**
         * 目标金额内容
         */
        public static final int CARD_ID_PROJECT_MONEY_CONTENT_ID = 2001;
    }

    public static class RequestCode {
        public static final int DEFAULT_RSC_CODE = 1000;
        public static final int RSC_LOGIN = 3003;

    }


    public static class Http {

        public final static int DEFAULT_REQ_CACHE_TIME = 60; //默认在线接口请求的缓存时间为60秒

        public static final int HTTP_CONNECTION_TIMEOUT = 15;
        /**
         * OkHttp3.2.0 bug https://github.com/square/okhttp/issues/1930,
         * work around to set read timeout to 0
         */
        public static final int HTTP_READ_TIMEOUT = 0;
        public static final int HTTP_WRITE_TIMEOUT = 15;

        /**
         * UserBean-Agent: Peduli/1.0 (Android5.0.1 API21)
         * Qsc-Peduli-Token:
         * Authorization: UID:Signature
         * ExpireIn:
         * Platform:Android
         * DeviceID:
         */

        public static final String HEADER_ACCEPT_KEY = "Accept";
        public static final String HEADER_ACCEPT_VALUE = "application/json";
        public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";

        public static final String HEADER_USER_AGENT = "User-Agent";
        public static final String HEADER_ACCESS_TOKEN = "Qsc-Peduli-Token";
        public static final String HEADER_AUTHORIZATION = "Authorization";
        public static final String HEADER_EXPIRES_IN = "ExpiresIn";
        public static final String HEADER_TOKEN_EXPIRES = "TokenExpires";
        public static final String HEADER_PLATFORM = "Platform";
        public static final String HEADER_PLATFORM_VALUE = "Android";
        public static final String HEADER_DEVICE_ID = "DeviceID";
        public static final String HEADER_DEVICE_LAN = "DeviceLan";
        public static final String HEADER_APP_VERSION = "Version";


        @SuppressLint("HardwareIds")
        public static String getDeviceId() {
            return Settings.Secure.getString(MyApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }




        /**
         * PeduliSehat/1.1.5 (Android5.0.1 API21)
         *
         * @return
         */
        public static String getUserAgent() {
            return String.format("GudangView/%s (%s%s API%s)", BuildConfig.VERSION_NAME, HEADER_PLATFORM_VALUE, android.os.Build.VERSION.RELEASE, android.os.Build.VERSION.SDK_INT);
        }

        /**
         * 获取设备语言
         *
         * @return
         */
        public static String getDeviceLan() {
            return LocaleUtil.getLanguage(MyApplication.getContext());
        }

        public static String getAppVersion() {
            return BuildConfig.VERSION_NAME;
        }



    }
}
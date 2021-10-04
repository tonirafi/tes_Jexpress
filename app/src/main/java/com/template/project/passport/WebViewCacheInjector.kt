//package com.qschou.pedulisehat.android.passport
//
//import android.os.Build
//import android.text.TextUtils
//import android.webkit.*
//import com.google.gson.Gson
//import com.s1rem.gudangview.MyApplication
//import com.s1rem.gudangview.http.bean.QSCToken
//import com.s1rem.gudangview.passport.Passport
//import java.lang.Exception
//import java.text.SimpleDateFormat
//import java.util.*
//
///**
// *   Created by 5Mall<zhangwei> on 2018/8/1
// *   Email:zhangwei@qingsongchou.com
// *   描述：
// */
//class WebViewCacheInjector private constructor() {
//
//    companion object {
//
//        private var instance: WebViewCacheInjector? = null
//
//        @JvmStatic
//        fun newInstance(): WebViewCacheInjector {
//            if (instance == null) {
//                synchronized(WebViewCacheInjector::class.java) {
//                    if (instance == null) {
//                        instance = WebViewCacheInjector()
//                    }
//                }
//            }
//
//            return instance!!
//        }
//
//    }
//
//    fun injectUserInfo(token: QSCToken?) {
//        token?.let {
//            syncCookie(".pedulisehat.id", "passport", Gson().toJson(it), it.covertExpiresToTimestamp(it.expiresIn))
//        }
//    }
//
//    fun injectSpecialDesInfo(specialDesc: SpecialDesc?) {
//        specialDesc?.let {
//            syncCookie(".pedulisehat.id", "groupDonate", Gson().toJson(it))
//        }
//    }
//
//    @Synchronized
//    private fun syncCookie(url: String, key: String, value: String, expiresIn: Long = System.currentTimeMillis() + 7 * 24 * 3600_000): Boolean {
//        /**
//         * java.lang.IllegalStateException: Call CookieSyncManager::createInstance() or create a webview before using this class
//         */
//        val wb = try {
//            WebView(MyApplication.getContext())
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        } ?: return false
//
//        val cookieManager = CookieManager.getInstance()
//        //如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
////        val cookie = "passport=${token.generatePassportCookies()};expires=365;path=/;domain=.pedulisehat.id"
//        cookieManager.setAcceptCookie(true)
//
//        val date = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
//                .format(Date(expiresIn))
////        cookieManager.setCookie(url, "expires=$date")
////        cookieManager.setCookie(url, "path=/")
////        cookieManager.setCookie(url, "domain=.pedulisehat.id")
//
//        cookieManager.setCookie(url, "$key=$value;expires=$date")
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // To get instant sync instead of waiting for the timer to trigger, the host can call this.
//            cookieManager.flush()
//        }
//
//        val newCookie = cookieManager.getCookie(url)
//        wb.destroy()
//        return !TextUtils.isEmpty(newCookie)
//    }
//
//    /**
//     * 判断webview中是否有passport cookie注入 如果没有 则执行护照同步
//     */
//    @Synchronized
//    fun syncCache() {
//        val cookieCache = CookieManager.getInstance().getCookie(".pedulisehat.id")
//        if (cookieCache.isNullOrEmpty()) {
//            Passport.instance.syncCache()
//        }
//    }
//}
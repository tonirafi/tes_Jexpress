//package com.s1rem.gudangview.passport
//
//import android.content.Context
//import android.os.Handler
//import android.os.Looper
//import com.s1rem.gudangview.MyApplication
//import com.s1rem.gudangview.http.bean.QSCToken
//
///**
// *   Created by 5Mall<zhangwei> on 2018/8/1
// *   Email:zhangwei@qingsongchou.com
// *   描述：传递给Passport 在每次保存token的时候 均会触发对Webview本地缓存的更新写入
// */
//class LocalStorageTokeManager(context: Context) : TokenManager(context) {
//    private val handler: Handler = Handler(Looper.getMainLooper())
//
//    override fun save(token: QSCToken, refreshedToken: Boolean) {
//        super.save(token,refreshedToken)
//        this.handler.post {
//            WebViewCacheInjector.newInstance().injectUserInfo(token)
//            if (!refreshedToken){
////                MyApplication.getContext().syncFcmToken(from = "SignIn")
//            }
//        }
//    }
//
//    override fun clear(signOut: Boolean) {
//        super.clear(signOut)
//        if (signOut) {
//            this.handler.post {
////                MyApplication.getContext().syncFcmToken(from = "SignOut")
//            }
//        }
//    }
//}
package com.template.project.router

import android.content.Context
import android.content.UriMatcher
import android.net.Uri
import cn.campusapp.router.interceptor.Interceptor

/**
 * Created by 5Mall<zhangwei> on 2018/7/18
 * Email:zhangwei@qingsongchou.com
 * 描述： 路由拦截器 可以对路由做拦截跳转等处理 该处拦截可拦截的路由地址 均为在路由表注册的地址
 */
class RouteInterceptor : Interceptor {

    companion object {
        const val CODE_LOGIN = 1
        // const val CODE_AD = 2
        const val CODE_SPLASH = 3
        const val CODE_MAIN = 4

        val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {

            this.addURI(RouterConstants.Common.HOST, RouterConstants.URI.LOGIN.path, CODE_LOGIN)
            //this.addURI(RouterConstants.Common.HOST, "go/ad", CODE_AD)
            this.addURI(RouterConstants.Common.HOST, RouterConstants.URI.SPLASH.path, CODE_SPLASH)
            this.addURI(RouterConstants.Common.HOST, RouterConstants.URI.MAIN.path, CODE_MAIN)
        }

    }

    private fun isUriNotNeedLogin(uri: Uri): Boolean {
        val match = sUriMatcher.match(uri)
        return when (match) {
            CODE_LOGIN, CODE_SPLASH, CODE_MAIN -> true
            else -> false
        }
    }

    override fun intercept(context: Context?, url: String?): Boolean {
//        if (isUriNotNeedLogin(Uri.parse(url))) {
//            //TODO 进行拦截跳转
//            return true
//        }

        return false
    }
}

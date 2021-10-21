package com.template.android.router

import android.net.Uri
import com.template.android.utils.LogUtil

object RouterConstants {

    object Common {
        const val SCHEME = "gv"
        const val HOST = "app"
        private const val BASE_URI = "$SCHEME://$HOST"
        private const val PATH_GO = "/go"
        private const val PATH_DO = "/do"
        const val GO_URI = BASE_URI + PATH_GO
        const val DO_URI = BASE_URI + PATH_DO

        const val DEFAULT_H5_HIDE_TITLE_BAR_VALUE = false

    }

    /***
     * Path 路由参数常量
     */
    object Params {

        const val H5_URL = "url"
        const val TITLE = "title"
        const val H5_AUTH = "auth"
        const val IS_FIXED_TITLE = "is_fixed_title"
        const val HIDE_TITLE = "hide_title"
        const val IKLAN_ID = "iklan_id"
        const val TAGS = "tags"
        const val FROM = "from"
        const val SHORT_LINK = "short_link"
        const val PRE_LOAD = "preload"
    }

    object Path {
        const val SPLASH = "splash"
        const val MAIN = "main"
        const val PEMBAYARAN_DETAIL = "PEMBAYARAN_DETAIL"
        const val LOGIN = "login"
        const val H5_JUMP = "jump"
    }

    object MAP_URI {
        val SPLASH = getUri(Common.GO_URI, Path.SPLASH)

        val LOGIN = getUri(Common.GO_URI, Path.LOGIN)
        val MAIN = getUri(Common.GO_URI, Path.MAIN)

        val PEMBAYARAN_DETAIL = getUri(Common.GO_URI, Path.PEMBAYARAN_DETAIL)
        val JUMP = getUri(Common.GO_URI, Path.H5_JUMP)


    }

    /**
     * 用于路由跳转
     */
    object URI {
        val SPLASH = Uri.parse(MAP_URI.SPLASH)!!

        val LOGIN = Uri.parse(MAP_URI.LOGIN)!!

        val MAIN = Uri.parse(MAP_URI.MAIN)!!
        val PEMBAYARAN_DETAIL = Uri.parse(MAP_URI.PEMBAYARAN_DETAIL)!!


        val JUMP = Uri.parse(MAP_URI.JUMP)!!


    }

    private fun getUri(baseUri: String, vararg paths: String): String {
        val builder = Uri.parse(baseUri).buildUpon()
        for (path in paths) {
            builder.appendPath(path)
        }
        val result = builder.build().toString()
        LogUtil.logD("add router :$result")
        return result
    }


    fun appendFromParamIfEmpty(uri: Uri, from: String): Uri {
        if (uri.getQueryParameter(Params.FROM).isNullOrEmpty()) {
            return uri.buildUpon().appendQueryParameter(Params.FROM, from).build()
        }

        return uri
    }

}

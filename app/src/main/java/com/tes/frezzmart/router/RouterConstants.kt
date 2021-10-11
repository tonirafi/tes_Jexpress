package com.tes.frezzmart.router

import android.net.Uri
import com.tes.frezzmart.utils.LogUtil

import java.util.regex.Pattern

object RouterConstants {

    object Common {
        const val SCHEME = "gv"
        const val HOST = "app"
        private const val BASE_URI = "$SCHEME://$HOST"
        private const val PATH_GO = "/go"
        private const val PATH_DO = "/do"
        const val GO_URI = BASE_URI + PATH_GO
        const val DO_URI = BASE_URI + PATH_DO

        const val DEFAULT_H5_HIDE_TITLE_BAR_VALUE = false //默认H5页面标题栏展示状态 true为隐藏标题栏  当H5页面控制标题展示状态的hide_title字段未传递的时候默认会使用该字段取值

        //default, search，share，firebase notification,  in-app message, H5 banner, campaigner banner, donor banner
        const val FROM_VAL_DEFAULT = "default"
        const val FROM_VAL_SEARCH = "search"
        const val FROM_VAL_FCM = "firebase notification"

        //        const val FROM_VAL_IN_APP_MSG = "in-app message"
        const val FROM_VAL_BANNER_CAMPAIGNER = "campaigner banner"
        const val FROM_VAL_BANNER_DONOR = "donor banner"
        const val FROM_VAL_AD_CAMPAIGNER = "campaigner ad"
        const val FROM_VAL_AD_DONOR = "donor ad"
    }

    /***
     * Path 路由参数常量
     */
    object Params {
        const val UUID = "uuid"
        const val INDEX = "index"
        const val ACTION_URL = "actionUrl"
        const val H5_URL = "url"
        const val ERROR_INFO = "errorInfo"
        const val TITLE = "title"
        const val VALUE = "value"
        const val H5_AUTH = "auth"
        const val IS_FIXED_TITLE = "is_fixed_title"
        const val HIDE_TITLE = "hide_title"
        const val ENABLE_REFRESH = "enable_refresh"
        const val CACHE_MODE = "cache_mode"
        const val DESCRIPTION = "description"
        const val IMAGE = "image"
        const val SHARE_TYPE = "share_type"
        const val HASH_TAG = "hashtag"
        const val QUOTE = "quote"
        const val USER_ID = "user_id"
        const val OPEN_ID = "open_id"
        const val INFLUEN_ID = "influen_id"
        const val IKLAN_JSON = "iklanJson"
        const val IKLAN_ID = "iklan_id"
        const val MOBILE = "mobile"
        const val MCC = "mobile_country_code"
        const val PLATFORM = "platform"
        const val REQ_CODE = "req_code"
        const val TYPE = "type"
        const val TYPE_NAME = "type_name"
        const val RS_JSON = "responseJson"
        const val HIDE_AGREEMENT = "hide_agreement"
        const val IS_HOST = "is_host"
        const val CLEAR_TOP = "clear_top"
        const val MAX_LENGTH = "max_length"
        const val SHARE_LINK = "share_link"
        const val TAGS = "tags"
        const val ORDER_ID = "order_id"
        const val ID = "id"
        const val NAME = "name"
        const val IS_ZAKAT = "is_zakat"
        const val FROM = "from"
        const val KEYWORD = "keyword"
        const val ACTION_NAME = "actionName"
        const val CODE = "code"
        const val ACTION = "action"
        const val DONOR_VERSION = "donorVersion"
        const val SHORT_LINK = "short_link"
        const val PRE_LOAD = "preload"
        const val PWD = "pwd"
        const val FORCE_HTTP = "force_http"
        const val STEP_2ND = "step_2nd"
        const val CATEGORY_IDS = "category_ids"
        const val GOPAY_ACTIVE = "gopay_active"
        const val GOPAY_ACCOUNT_ID = "gopay_account_id"
    }

    /***
     * Path 路由路径 用于分组
     * ps:里面不允许带下划线
     */
    object Path {
        const val SPLASH = "splash_gv"
        const val MAIN = "main_gv"
        const val IKLAN_DETAIL = "iklan_detail_gv"
        const val PENARIKAN = "penarikan_gv"
        const val IKLAN_DETAIL_WEB = "iklan_detail_web_gv"
        const val UPDATE_USER = "update_user_gv"
        const val UPDATE_IKLAN = "update_iklan_gv"
        const val UPDATE_AKUN_BANK = "update_akun_bank_gv"
        const val TRANSAKSI = "transaksi_gv"
        const val LOGIN = "login_gv"
        const val H5_JUMP = "jump_gv"
        const val FAQs = "faqs_gv"
        const val CHATROOM = "chat_room_gv"
        const val SIGN_OUT = "sign_out_gv"
        const val MARKET = "google_play_market_gv"
        const val SPECIAL_PROJECTS = "special_projects_gv"
    }

    /**
     * 仅页面路由的注册
     */
    object MAP_URI {
        val SPLASH = getUri(Common.GO_URI, Path.SPLASH)

        val LOGIN = getUri(Common.GO_URI, Path.LOGIN)
        val MAIN = getUri(Common.GO_URI, Path.MAIN)

        val IKLAN_DETAIL = getUri(Common.GO_URI, Path.IKLAN_DETAIL)

        val PENARIKAN = getUri(Common.GO_URI, Path.PENARIKAN)

        val IKLAN_DETAIL_WEB = getUri(Common.GO_URI, Path.IKLAN_DETAIL_WEB)

        val UPDATE_USER = getUri(Common.GO_URI, Path.UPDATE_USER)

        val UPDATE_IKLAN = getUri(Common.GO_URI, Path.UPDATE_IKLAN)

        val UPDATE_AKUN_BANK = getUri(Common.GO_URI, Path.UPDATE_AKUN_BANK)

        val TRANSAKSI = getUri(Common.GO_URI, Path.TRANSAKSI)

        val JUMP = getUri(Common.DO_URI, Path.H5_JUMP)

        val FAQs = getUri(Common.GO_URI, Path.FAQs)

        val CHATROOM = getUri(Common.GO_URI, Path.CHATROOM)

        val GOOGLE_PLAY_MARKET = getUri(Common.GO_URI, Path.MARKET)


        val SIGN_OUT = getUri(Common.DO_URI, Path.SIGN_OUT)

        val SPECIAL_PROJECTS = getUri(Common.GO_URI, Path.SPECIAL_PROJECTS)

    }

    /**
     * 用于路由跳转
     */
    object URI {
        //splash页面
        val SPLASH = Uri.parse(MAP_URI.SPLASH)!!

        //登陆页面
        val LOGIN = Uri.parse(MAP_URI.LOGIN)!!

        //首页-mainActivity
        val MAIN = Uri.parse(MAP_URI.MAIN)!!

        //h5 jump
        val JUMP = Uri.parse(MAP_URI.JUMP)!!

        val IKLAN_DETAIL = Uri.parse(MAP_URI.IKLAN_DETAIL)!!

        val PENARIKAN = Uri.parse(MAP_URI.PENARIKAN)!!

        val IKLAN_DETAIL_WEB = Uri.parse(MAP_URI.IKLAN_DETAIL_WEB)!!

        val UPDATE_AKUN_BANK = Uri.parse(MAP_URI.UPDATE_AKUN_BANK)!!

        val UPDATE_USER = Uri.parse(MAP_URI.UPDATE_USER)!!

        val UPDATE_IKLAN = Uri.parse(MAP_URI.UPDATE_IKLAN)!!

        val TRANSAKSI = Uri.parse(MAP_URI.TRANSAKSI)!!


        // 项目专题页
        val SPECIAL_PROJECTS = Uri.parse(MAP_URI.SPECIAL_PROJECTS)!!

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

    fun transToQscUrl(uri: Uri): String? {
        val matcher = Pattern.compile("http(s)?://[^\\s]*gudangview.com/[\\w]+").matcher(uri.toString())
        if (matcher.lookingAt()) {
            val shortLink = matcher.group()?.run {
                drop(lastIndexOf("/") + 1)
            }

            val uriBuilder = URI.SPECIAL_PROJECTS.buildUpon().appendQueryParameter(Params.SHORT_LINK, shortLink)

            uri.getQueryParameter(Params.FROM)?.let {
                uriBuilder.appendQueryParameter(Params.FROM, it)
            }

            // qsc://app.pedulisehat/go/special_projects?short_link=<xxx>&from=[xxx]
            return uriBuilder.build().toString()
        }

        //todo 新增其他url转换本地路由判断

        return null
    }

    fun appendFromParamIfEmpty(uri: Uri, from: String): Uri {
        if (uri.getQueryParameter(Params.FROM).isNullOrEmpty()) {
            return uri.buildUpon().appendQueryParameter(Params.FROM, from).build()
        }

        return uri
    }

}

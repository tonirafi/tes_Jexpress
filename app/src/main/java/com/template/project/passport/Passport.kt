//package com.s1rem.gudangview.passport
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.os.Build
//import android.webkit.CookieManager
//import android.webkit.CookieSyncManager
//import com.qschou.pedulisehat.android.passport.WebViewCacheInjector
//import com.s1rem.gudangview.http.bean.QSCToken
//
////import com.appsflyer.AppsFlyerLib
////import com.appsflyer.AppsFlyerProperties
//
//
///**
// *   Created by 5Mall<zhangwei> on 2018/7/20
// *   Email:zhangwei@qingsongchou.com
// *   描述：护照 用于保存用户登陆信息
// */
//class Passport private constructor() : ITokenManager {
//
//    private lateinit var context: Context
//    private lateinit var tokenManager: ITokenManager
//
//    override fun save(token: QSCToken, refreshedToken: Boolean) {
//        tokenManager.save(token, refreshedToken)
//        if (refreshedToken) {
////            !WebSocketManager.getInstance(context).isWsConnected
//        } else {
//            //注册成功、登录成功、初始化SDK后  调用 login 传入登录 ID
//            val uuid = "ps_uuid_${token.userId}"
////            SensorsDataAPI.sharedInstance().login(uuid)
////            if (uuid != "ps_uuid_${AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.APP_USER_ID)}") {
////                AppsFlyerLib.getInstance().setCustomerUserId(uuid)
////            }
//            true
//        }.takeIf { it }?.let { _ ->
//            getWssUrl(token)?.let {
//                loadUnreadNotificationCountFromCache()
////                WebSocketManager.getInstance(context).startConnect(it)
//            }
//        }
//    }
//
//    override fun get(): QSCToken? {
//        return tokenManager.get()
//    }
//
//    override fun clear(signOut: Boolean) {
//        tokenManager.clear(signOut)
//    }
//
//    override fun isExpired(token: QSCToken): Boolean {
//        return tokenManager.isExpired(token)
//    }
//
//    override fun newToken(token: QSCToken): QSCToken {
//        return tokenManager.newToken(token)
//    }
//
//    fun saveUser(userBean: UserBean?) {
//        if (userBean == null)
//            return
//
//        FirebaseAnalytics.getInstance(this.context).setUserId(userBean.user_id)
//        FirebaseAnalytics.getInstance(this.context).setUserProperty("Gender", userBean.genderDisPlay)
//
//        AppUtil.saveUser(userBean)
//
//        Freshchat.resetUser(context)
//        Freshchat.getInstance(context).identifyUser(userBean.user_id, null)
//
//    }
//
//    fun getCurUser(userId: String? = get()?.userId): UserBean? = try {
//        AppUtil.getUser(userId)
//    } catch (e: Exception) {
//        AppUtil.removeUser(userId)
//        null
//    }
//
//    fun updateCurUser(uid: String? = null, updateBlock: UserBean.() -> Unit): UserBean? {
//        return getCurUser(uid ?: get()?.userId)?.also {
//            updateBlock(it)
//            AppUtil.saveUser(it)
//        }
//    }
//
//    fun updateUsrRestoreId(userBean: UserBean?, restoreId: String) {
//        userBean?.also {
//            it.restore_id = restoreId
//            AppUtil.saveUser(it)
//        }
//    }
//
//    fun setFreshchatUserProperties(userBean: UserBean) {
//
//        val freshchat = Freshchat.getInstance(context)
//        freshchat.identifyUser(userBean.user_id, userBean.restore_id)
//
//        //Get the user object for the current installation
//        val freshUser = freshchat.user
//        freshUser.firstName = userBean.user_name
//        //freshUser.setLastName("Doe");
//        freshUser.email = userBean.email
//        freshUser.setPhone("+" + userBean.mobile_country_code, userBean.mobile)
//        //Call setUser so that the user information is synced with Freshchat's servers
//        Freshchat.getInstance(context).user = freshUser
//
//        /* Set any custom metadata to give agents more context, and for segmentation for marketing or pro-active messaging */
//        val userMeta = HashMap<String, String>()
//        if (!userBean.login_type.isNullOrEmpty()) {
//            userMeta["userLoginType"] = userBean.login_type
//        }
//        if (userBean.city_id > 0) {
//            userMeta["city_id"] = userBean.city_id.toString()
//        }
//
//        if (userBean.isOrg) {
//            userMeta["userType"] = "Organization"
//        }
//        userMeta["gender"] = userBean.genderDisPlay
////        userMeta["numTransactions"] = "5"
////        userMeta["usedWishlistFeature"] = "yes"
//
//        //Call setUserProperties to sync the user properties with Freshchat's servers
//        freshchat.setUserProperties(userMeta)
//    }
//
//    fun signOut(jumpToMain: Boolean = true) {
//        //clear token cache
//        clear(true)
//        //clear user info in database
//        clearUserInfoInDateBase()
//        //clear webView cookies
//        val cookieManager = CookieManager.getInstance()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            cookieManager.removeAllCookies {
//                if (jumpToMain) {
//                    IntentUtil.intentToMain(context, true, -1)
//                }
//            }
//        } else {
//            CookieSyncManager.createInstance(context) // CookieSyncManager如果没有创建，直接调用removeAllCookie就可能会导致app crash。
//            cookieManager.removeAllCookie()
//            if (jumpToMain) {
//                IntentUtil.intentToMain(context, true, -1)
//            }
//        }
//
//        //Reset user data
//        Freshchat.resetUser(context)
//
//        //神策
//        SensorsDataAPI.sharedInstance().logout()
//
//        resetUnreadNotificationCount()
//        WebSocketManager.getInstance(context)?.let {
//            it.stopConnect()
//            it.mockServerUnreadNotificationCount(0)
////            it.recycle()
//        }
//    }
//
//    private fun clearUserInfoInDateBase() {
//        val userId = get()?.userId ?: return
//        AppUtil.removeUser(userId)
//    }
//
//    fun syncCache(): QSCToken? {
//        val token = get()
//        if (token == null || token.isRefreshTokenExpired) {
//            signOut(false)
//        } else {
//            WebViewCacheInjector.newInstance().injectUserInfo(token)
//        }
//        return token
//    }
//
//    fun needLogin(): Boolean {
//        val token = get()
//        return token == null || token.isRefreshTokenExpired
//    }
//
//    fun getWssUrl(token: QSCToken? = get()): String? {
//        if (token == null || token.isExpired)
//            return null
//        //wss://mns-qa.pedulisehat.id/v1/ws?token=2d0a4dd85bc7ad9b972d18a900a4003f10
//        return "${BuildConfig.URL_DOMAIN_MNS.replace("https", "wss")}v1/ws?token=${token.accessToken}"
//    }
//
//    fun loadUnreadNotificationCountFromCache() {
//        NotificationInComingListener.LOAD_CACHE = true
//        NotificationInComingListener.USER_ID = get()?.userId
//        NotificationInComingListener.NUM = AppUtil.getUnreadNotificationCount(NotificationInComingListener.USER_ID)
//    }
//
//    fun resetUnreadNotificationCount() {
//        NotificationInComingListener.USER_ID = null
//        NotificationInComingListener.NUM = 0
//    }
//
//    init {
//        println("This ($this) is a singleton")
//    }
//
//    private object Holder {
//        @SuppressLint("StaticFieldLeak")
//        val INSTANCE = Passport()
//    }
//
//    companion object {
//        val instance: Passport by lazy { Holder.INSTANCE }
//    }
//
//    fun init(context: Context, tokenManager: ITokenManager) {
//        this.context = context.applicationContext
//        this.tokenManager = tokenManager
//    }
//
//}
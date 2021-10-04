package com.template.project.passport

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.template.project.http.bean.QSCToken
import com.template.project.utils.LogUtil
import java.util.concurrent.locks.ReentrantLock

/**
 *   Created by 5Mall<zhangwei> on 2018/7/26
 *   Email:zhangwei@qingsongchou.com
 *   描述：
 */
open class TokenManager(context: Context) : ITokenManager {

    private val lock = ReentrantLock()
    private val iToken = "QSCToken"
    private val sp: SharedPreferences = context.getSharedPreferences(iToken, 0)
    private var tokenCache: QSCToken? = null

    override fun save(token: QSCToken, refreshedToken: Boolean) {
        val t = System.currentTimeMillis()
        this.lock.lock()
        try {
            token.initExpiresTimeIfEmpty()
            val editor = this.sp.edit()
            editor.putString(iToken, Gson().toJson(token))
            editor.apply()
            this.tokenCache = token
        } finally {
            this.lock.unlock()
            LogUtil.logD("Duration:" + (System.currentTimeMillis() - t))
        }
    }

    override fun get(): QSCToken? {
        this.lock.lock()
        val var10: QSCToken?
        try {
            var10 = if (this.tokenCache != null) {
                tokenCache
            } else {
                val token = this.sp.getString(iToken, null)
                if (token.isNullOrBlank()) {
                    this.clear()
                    null
                } else {
                    Gson().fromJson(token, QSCToken::class.java)
                }
            }

        } finally {
            this.lock.unlock()
        }

        return var10
    }

    override fun clear(signOut: Boolean) {
        val t = System.currentTimeMillis()
        this.lock.lock()
        try {
            this.sp.edit().clear().apply()
            this.tokenCache = null
        } finally {
            this.lock.unlock()
            LogUtil.logD("Duration:" + (System.currentTimeMillis() - t))
        }
    }

    override fun isExpired(token: QSCToken): Boolean = token.isExpired

    override fun newToken(token: QSCToken): QSCToken {
        return QSCToken().apply {
            accessToken = token.accessToken
            tokenType = token.tokenType
            refreshToken = token.refreshToken
            signature = token.signature
            userId = token.userId
            expiresIn = token.expiresIn
            tokenExpires = token.tokenExpires
            serverTimestamp = token.serverTimestamp
        }
    }
}
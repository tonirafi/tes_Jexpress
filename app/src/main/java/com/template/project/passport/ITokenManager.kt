package com.template.project.passport

import com.template.project.http.bean.QSCToken


/**
 *   Created by 5Mall<zhangwei> on 2018/7/26
 *   Email:zhangwei@qingsongchou.com
 *   描述： token 管理接口
 */
interface ITokenManager {
    fun save(token: QSCToken, refreshedToken: Boolean = false)
    fun get(): QSCToken?
    fun clear(signOut: Boolean = false)
    fun isExpired(token: QSCToken): Boolean
    fun newToken(token: QSCToken): QSCToken
}
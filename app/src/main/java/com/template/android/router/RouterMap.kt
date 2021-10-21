package com.template.android.router

import android.app.Activity
import android.net.Uri
import cn.campusapp.router.router.IActivityRouteTableInitializer
import com.template.android.ui.pembayaran.PembayaranActivity


class RouterMap : IActivityRouteTableInitializer {

    companion object {
        private val urlState = HashMap<String, Boolean>()
        fun needLogin(uri: Uri?): Boolean {
            val key = uri?.let {
                String.format("%s://%s%s", uri.scheme, uri.authority, uri.path)
            } ?: return false

            return urlState[key] ?: false
        }
    }

    override fun initRouterTable(map: MutableMap<String, Class<out Activity>>) {
        addToMap(map, RouterConstants.MAP_URI.PEMBAYARAN_DETAIL, PembayaranActivity::class.java)

    }


    private fun addToMap(
        map: MutableMap<String, Class<out Activity>>,
        key: String,
        value: Class<out Activity>,
        needLogin: Boolean = false
    ) {

        if (map.containsKey(key))
            throw RuntimeException("this router [$key] has been registered!!!")

        map[key] = value
        urlState[key] = needLogin
    }
}

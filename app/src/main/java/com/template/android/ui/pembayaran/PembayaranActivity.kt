package com.template.android.ui.pembayaran

import android.os.Bundle
import com.template.android.R
import com.template.android.http.bean.ProductsItem
import com.template.android.router.RouterConstants
import com.template.android.ui.base.BaseActivity
import com.template.android.utils.AppUtil
import com.template.android.utils.StatusBarUtil
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import kotlinx.android.synthetic.main.pembayaran_activity.*

class PembayaranActivity: BaseActivity() {

    var totalPrice=0
    private var productsItem: ProductsItem? = null

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pembayaran_activity)
        initViews()
        StatusBarUtil.setDarkMode(this)
    }

    private fun initViews() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AppUtil.insertStatusBarHeight2TopPadding(toolbar)
        }
        toolbar.fitsSystemWindows = false
        toolbar.title = "Detail Pembayaran"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)
        val uri = parseIntent(intent)
        productsItem = uri?.getQueryParameter(RouterConstants.Params.PRE_LOAD)?.let {
            AppUtil.getGsonInstance().fromJson<ProductsItem>(it, ProductsItem::class.java)

        }

        totalPrice=productsItem?.price!!.toInt()

        ed_voucher.isEnabled = totalPrice <=35000

        if(ed_voucher.text.toString() =="ongkir5"){
            totalPrice -= 5000
        }else  if(ed_voucher.text.toString() =="disk35"){
            totalPrice -= (totalPrice * 35 / 100)
        }
    }

}
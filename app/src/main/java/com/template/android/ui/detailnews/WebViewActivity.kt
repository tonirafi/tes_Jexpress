package com.template.android.ui.detailnews

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.template.android.MyApplication
import com.template.android.R
import com.template.android.http.bean.ArticlesItem
import com.template.android.router.RouterConstants
import com.template.android.ui.base.BaseActivity
import com.template.android.utils.AppUtil
import com.template.android.utils.AppUtilNew
import kotlinx.android.synthetic.main.item_connection.*
import kotlinx.android.synthetic.main.item_empty_error.*
import kotlinx.android.synthetic.main.web_view_activity.*


open class WebViewActivity : BaseActivity() {

    private val PERMISSION_ALL = 1

    var uploadMessage: ValueCallback<Array<Uri>>? = null
    private var mUploadMessage: ValueCallback<Uri?>? = null
    val REQUEST_SELECT_FILE = 100
    private val FILECHOOSER_RESULTCODE = 1
    private var lastUrl = ""
    private var title = ""


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view_activity)


        val uri = parseIntent(intent)
        uri?.getQueryParameter(RouterConstants.Params.PRE_LOAD)?.let {
            var articles =
                AppUtil.getGsonInstance().fromJson<ArticlesItem>(it, ArticlesItem::class.java)
            lastUrl = articles.url!!
            title = articles.title!!
        }
        initView()

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AppUtil.insertStatusBarHeight2TopPadding(toolbar)
        }

        toolbar.fitsSystemWindows = false
        toolbar.title = title
        refreshLayout.isEnabled = false
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.text_green)



        btReload.setOnClickListener {
            web_view.loadUrl(lastUrl)
        }


        refreshLayout.setOnRefreshListener {
            web_view.loadUrl(lastUrl)
        }

        if (lastUrl.isNullOrEmpty()) {

            showMessage("Oops,url can not be empty.")
            onComplete()
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        web_view.apply {
            settings.javaScriptEnabled = true
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true
            settings.domStorageEnabled = true
            settings.allowContentAccess = true
            settings.allowFileAccess = true
            settings.allowFileAccess = true

        }



        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                refreshLayout.isRefreshing = false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                refreshLayout.isRefreshing = true
                checkConnection()

            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                empty_error.visibility = View.VISIBLE
                refreshLayout.isRefreshing = false

            }


        }


        web_view.webChromeClient = object : WebChromeClient() {
            protected fun openFileChooser(
                uploadMsg: ValueCallback<Uri?>?,
                acceptType: String?
            ) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE
                )
            }


            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onShowFileChooser(
                mWebView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (uploadMessage != null) {
//                    uploadMessage.onReceiveValue(null)
                    uploadMessage = null
                }
                uploadMessage = filePathCallback
                val intent = fileChooserParams.createIntent()
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE)
                } catch (e: ActivityNotFoundException) {
                    uploadMessage = null
//                    val show: Any =
//                        Toast.makeText(this, "Cannot Open File Chooser", Toast.LENGTH_LONG)
//                            .show()
                    return false
                }
                return true
            }

            //For Android 4.1 only
            protected fun openFileChooser(
                uploadMsg: ValueCallback<Uri?>?,
                acceptType: String?,
                capture: String?
            ) {
                mUploadMessage = uploadMsg
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "File Chooser"),
                    FILECHOOSER_RESULTCODE
                )
            }

            protected fun openFileChooser(uploadMsg: ValueCallback<Uri?>) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE
                )
            }
        }


        web_view.settings.javaScriptEnabled = true
        web_view.loadUrl(lastUrl)

    }


    override fun onBackPressed() {
        web_view.stopLoading()

        if (web_view.canGoBack()) {
            web_view.goBack()
//            progWebView.onWebViewPageGoBack()
        } else {
            //super.onBackPressed(); fix bugtag #3198, just finish
            onComplete()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null) return
                uploadMessage!!.onReceiveValue(
                    WebChromeClient.FileChooserParams.parseResult(
                        resultCode,
                        intent
                    )
                )
                uploadMessage = null
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
// Use RESULT_OK only if you're implementing WebView inside an Activity
            val result =
                if (intent == null || resultCode != RESULT_OK) null else intent.data
            mUploadMessage!!.onReceiveValue(result)
            mUploadMessage = null
        } else Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_LONG).show()
    }


    private fun hasPermissions(context: Context, permission: Array<String>): Boolean? {
        if (permission.isNotEmpty()) {
            for (i in permission.indices) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission[i]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.closeButton -> {
            onComplete()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        web_view?.let {
            it.handler?.removeCallbacksAndMessages(null)
            it.clearHistory()
            it.removeAllViews()
            refreshLayout.removeView(web_view)
            it.destroy()
        }
        super.onDestroy()
    }

    fun checkConnection() {
        if (!AppUtilNew.isNetworkAvailable(MyApplication.getContext())) {
            tvInfo.text = "Failed load the content."
            empty_error.visibility = View.VISIBLE
            lnrlConnection.visibility = View.VISIBLE
            web_view.visibility = View.GONE
        } else {
            empty_error.visibility = View.GONE
            lnrlConnection.visibility = View.GONE
            web_view.visibility = View.VISIBLE

        }
    }


}
package com.template.project

import android.app.Activity
import androidx.multidex.MultiDexApplication
import cn.campusapp.router.Router
import com.template.project.router.RouteInterceptor
import com.template.project.router.RouterConstants
import com.template.project.router.RouterMap
import com.template.project.ui.base.BaseView
import io.reactivex.plugins.RxJavaPlugins
import java.io.File
import java.util.*



open class MyApplication : MultiDexApplication() {

    private val ONESIGNAL_APP_ID: String? = "b980e1fd-6ddc-4d77-86c6-93d5cfd7d8ac"

    private val activities = Stack<Activity>()

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        Router.initActivityRouter(this, RouterMap(), RouterConstants.Common.SCHEME)
        Router.setDebugMode(BuildConfig.DEBUG)
        Router.setInterceptor(RouteInterceptor())


        //全局性的错误处理 只有当观察者订阅后Observable后 且无注册错误处理回调时 执行发生的错误才会在此被处理 避免出现OnErrorNotImplementedException异常
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
//            mainThreadToast(ApiExceptionHelper.getDisplayError(it))
        }

        /**
         * Firebase 公有话题订阅
         */
        arrayOf(
            "root",
            "Android",
            "Android_v${BuildConfig.VERSION_NAME}",
            "Android_${android.os.Build.VERSION.RELEASE}"
        )
                .forEach {
                    subscribeTopic(it)
                }


        subscribeTopic(applicationContext.packageName)

    }







    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
//        super.registerActivityLifecycleCallbacks(callback)
//        if (callback is FirebaseInAppMessagingDisplay) {
//            firebaseInAppMessagingCallback = callback
//        }
    }

    private fun subscribeTopic(topic: String) {
//        FirebaseMessaging.getInstance().subscribeToTopic(topic)
//                .addOnCompleteListener { task ->
//                    val msg = if (task.isSuccessful) {
//                        "subscribe successfully"
//                    } else {
//                        "subscribe failed"
//                    }
//                    LogUtil.logD("Topic[$topic] $msg")
//                }
    }



    fun mainThreadToast(message: String) {
        with(activities.peek() as BaseView) {
            //           this.getPresenter().refresh()
            this.showMessage(message)
        }
    }

    fun getAppCacheDir(fileName: String = "picasso-file-cache"): File {
        val cache = File(getContext().cacheDir, fileName)
        if (!cache.exists()) {
            cache.mkdirs()
        }
        return cache
    }

    companion object {
        private const val TAG = "MyApplication"
        private lateinit var mInstance: MyApplication

        @JvmStatic
        fun getContext(): MyApplication {
            return mInstance
        }

    }

}
package com.template.android

import android.app.Activity
import androidx.multidex.MultiDexApplication
import cn.campusapp.router.Router
import com.template.android.di.appModule
import com.template.android.router.RouteInterceptor
import com.template.android.router.RouterConstants
import com.template.android.router.RouterMap
import com.template.android.ui.base.BaseView
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*


open class MyApplication : MultiDexApplication() {


    private val activities = Stack<Activity>()

    override fun onCreate() {
        super.onCreate()
        mInstance = this


        Router.initActivityRouter(this, RouterMap(), RouterConstants.Common.SCHEME)
        Router.setDebugMode(BuildConfig.DEBUG)
        Router.setInterceptor(RouteInterceptor())


        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
//            mainThreadToast(ApiExceptionHelper.getDisplayError(it))
        }

    }


    fun mainThreadToast(message: String) {
        with(activities.peek() as BaseView) {
            //           this.getPresenter().refresh()
            this.showMessage(message)
        }
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
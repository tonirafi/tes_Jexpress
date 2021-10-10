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


    private val activities = Stack<Activity>()

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        Router.initActivityRouter(this, RouterMap(), RouterConstants.Common.SCHEME)
        Router.setDebugMode(BuildConfig.DEBUG)
        Router.setInterceptor(RouteInterceptor())


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
package com.template.android.ui.home

import com.template.android.AppConstants
import com.template.android.http.api.ApiService
import com.template.android.http.bean.NewsResponse
import com.template.android.http.bean.Warung
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent

class HomeRepository(private val newsApi: ApiService) : KoinComponent {

    fun getWarung(
        cacheControl: String?
    ): Observable<Warung>? {
        return newsApi?.getWarung(
            cacheControl
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }



}
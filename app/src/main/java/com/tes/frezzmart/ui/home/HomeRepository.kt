package com.tes.frezzmart.ui.home

import androidx.lifecycle.LiveData
import com.tes.frezzmart.AppConstants
import com.tes.frezzmart.http.api.ApiService
import com.tes.frezzmart.http.bean.NewsResponse
import com.tes.frezzmart.room.NewsDao
import com.tes.frezzmart.room.NewsModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent

class HomeRepository(private val newsApi: ApiService, private val newsDao: NewsDao):KoinComponent {

    fun loadDataNews(pageIndex:Int, search:String, cacheControl:String?): Observable<NewsResponse>? {
      return  newsApi?.listNews(search,pageIndex,15,"publishedAt" ,AppConstants.TOKEN, cacheControl)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())

    }

    fun loadDataNewsLocal(): LiveData<List<NewsModel>>? {
        return  newsDao?.getAllNews()

    }


}
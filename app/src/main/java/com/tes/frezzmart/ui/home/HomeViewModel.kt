package com.tes.frezzmart.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tes.frezzmart.adapter.BaseCard
import com.tes.frezzmart.adapter.card.NewsItemCard
import okhttp3.CacheControl
import java.util.concurrent.TimeUnit

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private var pageIndex = 1

    val listBaseCardPre by lazy {
        MutableLiveData<ArrayList<BaseCard>>()
    }

    val listBaseCard by lazy {
        MutableLiveData<ArrayList<BaseCard>>()
    }

    val listBaseCardLoadMore by lazy {
        MutableLiveData<ArrayList<BaseCard>>()
    }


    val throwable by lazy {
        MutableLiveData<Throwable>()
    }

    @SuppressLint("CheckResult")
    fun loadMore(vertical:Boolean=true){
        pageIndex++
        homeRepository.loadDataNews(pageIndex, null)
            ?.map {
                var dataArticles= it.articles
                var baseCards = ArrayList<BaseCard>()

                for ( articel in dataArticles!!) {
                        baseCards.add(NewsItemCard(articel))
                }

                baseCards
            }
            ?.subscribe(
                { data ->
                    listBaseCardLoadMore.postValue(data)
                    if (pageIndex > 1 && data.isEmpty()) {
                        pageIndex--
                    }
                },
                { error ->
                    if (pageIndex > 1) {
                        pageIndex--
                    }
                    throwable.postValue(error)
                })

    }

    @SuppressLint("CheckResult")
    fun loadDataHome(forceHttp: Boolean = false){
        pageIndex=1
        val cacheControl = if (forceHttp) CacheControl.Builder()
            .maxAge(10, TimeUnit.SECONDS)
            .build()
            .toString() else null
        homeRepository.loadDataNews(pageIndex, cacheControl!!)
            ?.map {
                var dataArticles= it.articles
                var baseCards = ArrayList<BaseCard>()

                for ( articel in dataArticles!!) {
                    baseCards.add(NewsItemCard(articel))

                 }

            baseCards
            }
            ?.subscribe({
                listBaseCard.postValue(it)

            },{
                throwable.postValue(it)
            })
    }



    fun preloadCards(vertical: Boolean=true) {
        val baseCards = ArrayList<BaseCard>()

        for (i in 0..7) {
                baseCards.add(NewsItemCard(null).apply {
                    loading = true
                })
            }

        listBaseCardPre.postValue(baseCards)

    }


}
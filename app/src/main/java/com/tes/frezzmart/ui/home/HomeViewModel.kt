package com.tes.frezzmart.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tes.frezzmart.adapter.BaseCard
import com.tes.frezzmart.adapter.card.DateItemCard
import com.tes.frezzmart.adapter.card.EmptyErrorItemCard
import com.tes.frezzmart.adapter.card.NewsItemCard
import okhttp3.CacheControl
import java.util.concurrent.TimeUnit

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private var pageIndex = 1
    private var default = "bisnis"

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


    fun cardOffline(){
        var baseCards = ArrayList<BaseCard>()
        baseCards.add(EmptyErrorItemCard("Empty Result.",true))
        listBaseCard.postValue(baseCards)
    }


    @SuppressLint("CheckResult")
    fun loadDataHome(forceHttp: Boolean = false,search:String){
        pageIndex=1
        val cacheControl = if (forceHttp) CacheControl.Builder()
            .maxAge(10, TimeUnit.SECONDS)
            .build()
            .toString() else null
                var  dataSearch = if(search == ""){
                    default
                }else{
                    search
                }



        homeRepository.loadDataNews(pageIndex,dataSearch, cacheControl!!)
            ?.map {


                var dataArticles= it.articles
                var baseCards = ArrayList<BaseCard>()

                if(dataArticles!!.isEmpty() || dataArticles==null){
                    baseCards.add(EmptyErrorItemCard("Empty Result.",false))

                }else{
                    var date=""
                    for ( articel in dataArticles!!) {
                        if(date !=articel?.getDate()){
                            date= articel?.getDate()!!
                            baseCards.add(DateItemCard(articel))
                        }
                        baseCards.add(NewsItemCard(articel))

                    }
                }


            baseCards
            }
            ?.subscribe({
                listBaseCard.postValue(it)

            },{
                throwable.postValue(it)
            })
    }

    @SuppressLint("CheckResult")
    fun loadMore(search:String){
        pageIndex++
        var  dataSearch = if(search == ""){
            default
        }else{
            search
        }



        homeRepository.loadDataNews(pageIndex,dataSearch, null)
            ?.map {
                var dataArticles= it.articles
                var baseCards = ArrayList<BaseCard>()
                var date=""
                for ( articel in dataArticles!!) {
                    if(date !=articel?.getDate()){
                        date= articel?.getDate()!!
                        baseCards.add(DateItemCard(articel))
                    }
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
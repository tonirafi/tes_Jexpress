package com.template.android.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.template.android.adapter.BaseCard
import com.template.android.adapter.card.EmptyErrorItemCard
import com.template.android.adapter.card.ProdakItemCard
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


    fun cardOffline() {
        var baseCards = ArrayList<BaseCard>()
        baseCards.add(EmptyErrorItemCard("Empty Result.", true))
        listBaseCard.postValue(baseCards)
    }


    @SuppressLint("CheckResult")
    fun loadDataHome(forceHttp: Boolean = false, search: String) {
        pageIndex = 1
        val cacheControl = if (forceHttp) CacheControl.Builder()
            .maxAge(10, TimeUnit.SECONDS)
            .build()
            .toString() else null


        homeRepository.getWarung(cacheControl!!)
            ?.map {

                var dataProdak = it.products

                    dataProdak!!.filter {
                            it?.name.toString() in search
                        }

                    dataProdak!!.filter {

                        it?.category.toString() in search
                    }

                var baseCards = ArrayList<BaseCard>()

                if (dataProdak!!.isEmpty() || dataProdak == null) {
                    baseCards.add(EmptyErrorItemCard("Empty Result.", false))

                } else {
                    for (prodak in dataProdak!!) {

                        baseCards.add(ProdakItemCard(prodak))

                    }
                }


                baseCards
            }
            ?.subscribe({
                listBaseCard.postValue(it)

            }, {
                throwable.postValue(it)
            })
    }




    fun preloadCards(vertical: Boolean = true) {
        val baseCards = ArrayList<BaseCard>()

        for (i in 0..7) {
            baseCards.add(ProdakItemCard(null).apply {
                loading = true
            })
        }

        listBaseCardPre.postValue(baseCards)

    }


}
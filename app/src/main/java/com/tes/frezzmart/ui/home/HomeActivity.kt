package com.tes.frezzmart.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.aspsine.swipetoloadlayout.OnLoadMoreListener
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.tes.frezzmart.R
import com.tes.frezzmart.adapter.BaseCard
import com.tes.frezzmart.adapter.CardAdapter
import com.tes.frezzmart.adapter.card.NewsItemCard
import com.tes.frezzmart.ui.base.BaseActivity
import com.tes.frezzmart.utils.StatusBarUtil
import kotlinx.android.synthetic.main.layout_card_list_common.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity : BaseActivity(), OnRefreshListener, OnLoadMoreListener, CardAdapter.OnItemClickListener {

    private  val dashboardViewModel: HomeViewModel by viewModel()
    private val cardAdapter: CardAdapter = CardAdapter(this)
    private var vertical: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_card_list_common)
        initViews()
        setViewModel()
        StatusBarUtil.setDarkMode(this)
    }



    private fun initViews() {
        cardAdapter.setOnItemClickListener(this)
//        toolbar.fitsSystemWindows = false
//        toolbar.title = "Awesome App"
//        toolbar.setTitleTextColor(resources.getColor(R.color.white))
//        setSupportActionBar(toolbar)
        globalSwapRecyclerView.setAdapter(cardAdapter)
        dashboardViewModel.preloadCards(false)
        getDataHome()
        globalSwapRecyclerView.setRefreshEnabled(true)
        globalSwapRecyclerView.setOnRefreshListener(this)
        globalSwapRecyclerView.isLoadMoreEnabled = false
        globalSwapRecyclerView.setOnLoadMoreListener(this)

    }


    private fun setViewModel(){

        dashboardViewModel.listBaseCardPre.observe(this, Observer {
            hideAnimationOrLoading()
            updateCards(it,true)
            globalSwapRecyclerView.onCompleteRefresh()
        })
        dashboardViewModel.listBaseCard.observe(this, Observer {
            hideAnimationOrLoading()
            updateCards(it, true)
            globalSwapRecyclerView.onCompleteRefresh()
        })

        dashboardViewModel.listBaseCardLoadMore.observe(this, Observer {
            hideAnimationOrLoading()
            updateCards(it, false)
            globalSwapRecyclerView.onCompleteRefresh()
        })

        dashboardViewModel.throwable.observe(this, Observer {
            hideAnimationOrLoading()
            showMessage(it)
            globalSwapRecyclerView.onCompleteRefresh()
        })
    }

    fun updateCards(list: List<BaseCard>, reloadAll: Boolean) {


        if (reloadAll) {
            val enable = list.lastOrNull()?.let { it is NewsItemCard} ?: false
            globalSwapRecyclerView.isLoadMoreEnabled=enable

            cardAdapter.list.clear()
            cardAdapter.addAll(list)
            return
        }

        if (list.isEmpty()) {
            globalSwapRecyclerView.onCompleteRefresh()
            globalSwapRecyclerView.isLoadMoreEnabled=false
            return
        }

        val insertIndex = cardAdapter.list.size
        cardAdapter.list.addAll(insertIndex, list)
        cardAdapter.notifyItemRangeInserted(insertIndex, list.size)
    }


    override fun onRefresh() {
        getDataHome()
    }

    override fun onLoadMore() {
       dashboardViewModel.loadMore(vertical)
    }

    override fun onItemOnclick(position: Int) {

//        val baseCard = cardAdapter.list[position]
//        if (baseCard is ImageItemCard && baseCard.imageData != null) {
//                IntentUtil.intentToDetailImage(this, baseCard.imageData)
//            }
//
//        if (baseCard is ImageItemGridCard && baseCard.imageData != null) {
//            IntentUtil.intentToDetailImage(this, baseCard.imageData)
//        }


    }


    fun getDataHome(){
        dashboardViewModel.loadDataHome(true)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        this.menuInflater.inflate(R.menu.list_grid, menu)
//
//        this@HomeActivity.menuGroup = (menu?.findItem(R.id.grid) to menu?.findItem(R.id.list)).also { t ->
//            t.toList().forEach {
//                it?.actionView?.setOnClickListener { _ ->
//                    onOptionsItemSelected(it)
//                }
//            }
//        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.grid -> {
//            vertical=false
//            cardAdapter.clearList()
//            dashboardViewModel.preloadCards(vertical)
//            getDataHome()
//            setGridOrList()
//            true
//        }

        else -> super.onOptionsItemSelected(item)
    }




}
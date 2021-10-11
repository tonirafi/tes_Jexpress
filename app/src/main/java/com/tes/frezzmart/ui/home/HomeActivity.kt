package com.tes.frezzmart.ui.home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.aspsine.swipetoloadlayout.OnLoadMoreListener
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.tes.frezzmart.R
import com.tes.frezzmart.adapter.BaseCard
import com.tes.frezzmart.adapter.CardAdapter
import com.tes.frezzmart.adapter.card.NewsItemCard
import com.tes.frezzmart.ui.base.BaseActivity
import com.tes.frezzmart.utils.AppUtil
import com.tes.frezzmart.utils.StatusBarUtil
import kotlinx.android.synthetic.main.layout_card_list_common.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class HomeActivity : BaseActivity(), OnRefreshListener, OnLoadMoreListener, CardAdapter.OnItemClickListener {

    private  val dashboardViewModel: HomeViewModel by viewModel()
    private val cardAdapter: CardAdapter = CardAdapter(this)
    private var search = "kosong"
    var myMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_card_list_common_with_toolbar)
        initViews()
        setViewModel()
        StatusBarUtil.setDarkMode(this)
    }



    private fun initViews() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AppUtil.insertStatusBarHeight2TopPadding(toolbar)
        }
        cardAdapter.setOnItemClickListener(this)
        toolbar.fitsSystemWindows = false
        toolbar.title = "News App"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)
        globalSwapRecyclerView.setAdapter(cardAdapter)
        dashboardViewModel.preloadCards(false)
        getDataHome()
        globalSwapRecyclerView.setRefreshEnabled(true)
        globalSwapRecyclerView.setOnRefreshListener(this)
        globalSwapRecyclerView.isLoadMoreEnabled = false
        globalSwapRecyclerView.setOnLoadMoreListener(this)

        edSearch.addTextChangedListener(
            object : TextWatcher {

                private var timer = Timer()
                private val DELAY: Long = 500 // milliseconds
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                search=edSearch.text.toString()
                                getDataHome()
                            }
                        },
                        DELAY
                    )
                }
            }
        )
    }


    private fun setViewModel(){

        dashboardViewModel.listBaseCardPre.observe(this, Observer {
            hideAnimationOrLoading()
            updateCards(it, true)
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
       dashboardViewModel.loadMore(search)
    }

    override fun onItemOnclick(position: Int) {

    }


    fun getDataHome(){
        dashboardViewModel.loadDataHome(true, search)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu, menu)
        myMenu=menu
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.search -> {
                edSearch.visibility = View.VISIBLE
                edSearch.isFocusable = true
                item.isVisible = false
                edSearch.requestFocus()
                val imm: InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(edSearch, InputMethodManager.SHOW_IMPLICIT)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (edSearch.isVisible) {
            edSearch.visibility=View.GONE
            val item: MenuItem = myMenu!!.findItem(R.id.search)
            item.isVisible = true
        } else {
            onComplete()
        }
    }


}
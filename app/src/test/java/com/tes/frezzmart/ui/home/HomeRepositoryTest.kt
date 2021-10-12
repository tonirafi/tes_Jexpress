package com.tes.frezzmart.ui.home

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tes.frezzmart.di.appModule
import com.tes.frezzmart.http.bean.NewsResponse
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.*
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class HomeRepositoryTest: KoinTest {

    private val homeRepository: HomeRepository by inject()
    @Mock
    private lateinit var context: Application
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()



    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Before
    fun setup() {

        startKoin {
            androidContext(context)
            modules(appModule)
        }
    }

    @After
    fun teardown() {
        stopKoin()
    }


    @Test
    fun shouldShowData() {
        val testObserver = TestObserver<NewsResponse>()

        homeRepository!!.loadDataNews(1, "Bisnis", "")?.subscribe(testObserver)
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)

    }

    @Test
    fun getDataNews() {
        homeRepository!!.loadDataNews(1, "Bisnis", "")

    }

}
package com.template.android.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable

interface NewsDao {

    @Query("select * from newsTable")
    fun getAllNews(): Observable<List<NewsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNews(addNews: NewsModel)


    @Delete
    fun deleteNews(deleteNews: NewsModel)
}
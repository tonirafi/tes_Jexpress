package com.tes.frezzmart.room

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface NewsDao {

    @Query("select * from newsTable")
    fun getAllNews(): LiveData<List<NewsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNews(addNews: NewsModel)


    @Delete
    fun deleteNews(deleteNews: NewsModel)
}
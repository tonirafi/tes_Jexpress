package com.template.android.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [NewsModel::class], version = 1, exportSchema = false)
abstract class NewsDB : RoomDatabase() {
    abstract val newsDao: NewsDao
}
package com.tes.frezzmart.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.tes.frezzmart.http.bean.Source


@Entity(tableName = "newsTable")
class NewsModel {
    @ColumnInfo(name = "publishedAt")
    val publishedAt: String? = null

    @ColumnInfo(name = "author")
    val author: String? = null

    @ColumnInfo(name = "urlToImage")
    val urlToImage: String? = null

    @ColumnInfo(name = "description")
    val description: String? = null

    @ColumnInfo(name = "source")
    val source: Source? = null

    @ColumnInfo(name = "title")
    val title: String? = null

    @ColumnInfo(name = "url")
    val url: String? = null

    @ColumnInfo(name = "content")
    val content: String? = null
}
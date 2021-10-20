package com.template.android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.template.android.http.bean.Source
import com.template.android.utils.AppUtilNew


@Entity(tableName = "newsTable")
class NewsModel {
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0

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

    fun getDate(): String {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AppUtilNew.formatDate(publishedAt!!)
        } else ({
        }).toString()
    }
}
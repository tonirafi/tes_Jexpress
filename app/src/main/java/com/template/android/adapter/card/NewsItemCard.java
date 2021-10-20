package com.template.android.adapter.card;


import com.template.android.adapter.BaseCard;
import com.template.android.http.bean.ArticlesItem;

public class NewsItemCard extends BaseCard {
    public ArticlesItem articlesItem;

    public NewsItemCard(ArticlesItem articlesItem) {
        this.articlesItem = articlesItem;
    }


}

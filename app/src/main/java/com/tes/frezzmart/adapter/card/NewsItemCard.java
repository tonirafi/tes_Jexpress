package com.tes.frezzmart.adapter.card;


import com.tes.frezzmart.adapter.BaseCard;
import com.tes.frezzmart.http.bean.ArticlesItem;

public class NewsItemCard extends BaseCard {
    public ArticlesItem articlesItem;

    public NewsItemCard(ArticlesItem articlesItem) {
            this.articlesItem=articlesItem;
    }


}

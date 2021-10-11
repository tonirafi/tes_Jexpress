package com.tes.frezzmart.adapter.card;


import com.tes.frezzmart.adapter.BaseCard;
import com.tes.frezzmart.http.bean.ArticlesItem;

public class EmptyErrorItemCard extends BaseCard {
    public String title;
    public Boolean showButton;

    public EmptyErrorItemCard(String title,Boolean showButton) {
            this.title=title;
            this.showButton=showButton;
    }


}

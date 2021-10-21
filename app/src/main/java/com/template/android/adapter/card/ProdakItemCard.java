package com.template.android.adapter.card;


import com.template.android.adapter.BaseCard;
import com.template.android.http.bean.ProductsItem;

public class ProdakItemCard extends BaseCard {
    public ProductsItem productsItem;

    public ProdakItemCard(ProductsItem productsItem) {
        this.productsItem = productsItem;
    }


}

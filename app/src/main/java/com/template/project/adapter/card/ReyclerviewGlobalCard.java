package com.template.project.adapter.card;


import com.template.project.adapter.BaseCard;
import com.template.project.adapter.ItemViewProvider;

import java.util.List;

public class ReyclerviewGlobalCard<T> extends BaseCard {
    public ItemViewProvider cardProvider;
    public List<T> listData;
    public Integer spanCount;


    public ReyclerviewGlobalCard(List<T> listData, ItemViewProvider provider) {
        this.listData = listData;
        this.cardProvider = provider;
    }
}

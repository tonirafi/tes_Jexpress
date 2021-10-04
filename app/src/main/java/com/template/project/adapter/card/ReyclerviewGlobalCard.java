package com.template.project.adapter.card;


import com.template.project.adapter.BaseCard;
import com.template.project.adapter.ItemViewProvider;

import java.util.List;

/**
 * Created by 5Mall<zhangwei> on 2019-10-24
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class ReyclerviewGlobalCard<T> extends BaseCard {
    public ItemViewProvider cardProvider;
    public List<T> listData;
    public Integer spanCount;


    public ReyclerviewGlobalCard(List<T> listData, ItemViewProvider provider) {
        this.listData = listData;
        this.cardProvider = provider;
    }
}

package com.template.project.adapter.card;


import com.template.project.adapter.BaseCard;

import java.util.ArrayList;

/**
 * Created by 5Mall<zhangwei> on 2019-07-10
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class TabCard extends BaseCard {
    public  int posision;
    public ArrayList<String> namesTab;
    public TabCard(int posision,ArrayList<String> namesTab) {
        this.posision=posision;
        this.namesTab=namesTab;
    }


}

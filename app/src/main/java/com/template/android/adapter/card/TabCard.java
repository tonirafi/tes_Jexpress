package com.template.android.adapter.card;


import com.template.android.adapter.BaseCard;

import java.util.ArrayList;


public class TabCard extends BaseCard {
    public int posision;
    public ArrayList<String> namesTab;

    public TabCard(int posision, ArrayList<String> namesTab) {
        this.posision = posision;
        this.namesTab = namesTab;
    }


}

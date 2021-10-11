package com.tes.frezzmart.adapter.card;


import com.tes.frezzmart.adapter.BaseCard;

import java.util.ArrayList;


public class TabCard extends BaseCard {
    public  int posision;
    public ArrayList<String> namesTab;
    public TabCard(int posision,ArrayList<String> namesTab) {
        this.posision=posision;
        this.namesTab=namesTab;
    }


}

package com.tes.frezzmart.adapter;

import android.text.TextUtils;


import java.util.List;


public class CardMapInitializer implements ICardMapInitializer {

    private static final String CARD_DIR = "com.template.project.adapter.card.";
    private static final String PROVIDER_DIR = "com.template.project.adapter.providers.";

    @Override
    public void initRouterTable(List<String> cardNameList, List<String> providerNameList) {
        addCardProviderPair(cardNameList, providerNameList, "LabelCard", "LabelCardProvider");
        addCardProviderPair(cardNameList, providerNameList, "VPListCard", "VPListCardProvider");
        addCardProviderPair(cardNameList, providerNameList, "MoreCard", "MoreCardProvider");

    }


    /**
     * @param cardNameList
     * @param providerNameList
     * @param cardName
     * @param providerName
     */
    public void addCardProviderPair(List<String> cardNameList, List<String> providerNameList, String cardName, String providerName) {
        if (TextUtils.isEmpty(cardName) || TextUtils.isEmpty(providerName))
            return;

        if (cardName.contains("."))
            cardNameList.add(cardName);
        else
            cardNameList.add(CARD_DIR + cardName);


        if (providerName.contains("."))
            providerNameList.add(providerName);
        else
            providerNameList.add(PROVIDER_DIR + providerName);

    }
}

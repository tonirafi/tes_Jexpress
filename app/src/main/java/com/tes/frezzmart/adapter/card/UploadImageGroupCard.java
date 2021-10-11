package com.tes.frezzmart.adapter.card;

import android.text.TextUtils;

import androidx.annotation.NonNull;


import com.tes.frezzmart.adapter.EditCard;
import com.tes.frezzmart.service.upload.image.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

public class UploadImageGroupCard extends EditCard {
    public List<UploadImageUnitCard> cards;
    public int maxNum = 8;
    public boolean showCoverTag = true;
    public int placeholderDrawableRes = -1;

    public UploadImageGroupCard() {
        this._key = "images";
    }

    public UploadImageGroupCard(List<UploadImageUnitCard> cards) {
        this.cards = cards;
    }

    @Override
    public List<ImageBean> getValue() {
        List<ImageBean> value = new ArrayList<>(8);
        if (cards != null) {
            for (UploadImageUnitCard card : cards) {

                if (card.isAddCard || card.imageBean == null)
                    continue;

                if (card.imageBean.isUploadedLocalBean() || card.imageBean.isRemoteBean()) {
                    value.add(card.imageBean);
                }
            }
        }
        return value;
    }

    public boolean isUploading() {
        if (cards != null) {
            for (UploadImageUnitCard card : cards) {
                if (card.isAddCard || card.imageBean == null)
                    continue;

                if (card.imageBean.isNotYetUploadedLocalBean()) {
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<String> getSelectedPhotos() {
        ArrayList<String> list = new ArrayList<>(8);
        if (cards != null) {
            for (UploadImageUnitCard card : cards) {
                if (!card.isAddCard && card.imageBean != null) {
                    if (!TextUtils.isEmpty(card.imageBean.getImgPath())) {
                        list.add(card.imageBean.getImgPath());
                    } else if (!TextUtils.isEmpty(card.imageBean.getImageUrl())) {
                        list.add(card.imageBean.getCompatibleImageUrl());
                    }
                }
            }
        }

        return list;
    }

    public void update(String... localImages) {
        if (localImages == null || localImages.length == 0) {
            if (this.cards != null && !this.cards.isEmpty()) {
                this.cards.clear();
            }
            return;
        }

        int sum = localImages.length > maxNum ? maxNum : localImages.length;
        List<UploadImageUnitCard> imageCards = new ArrayList<>(sum);
        for (int i = 0; i < sum; i++) {
            imageCards.add(transToCard(localImages[i]));
        }

        this.cards = imageCards;
    }

    private UploadImageUnitCard transToCard(@NonNull String imgPath) {
        if (cards != null) {
            for (UploadImageUnitCard card : cards) {
                if (card.imageBean != null) {
                    if (imgPath.startsWith("http")) {
                        if (imgPath.equals(card.imageBean.getImageUrl()) && card.imageBean.isRemoteBean())
                            return card;
                    } else if (imgPath.equals(card.imageBean.getImgPath())) {
                        return card;
                    }
                }
            }
        }

        return new UploadImageUnitCard(new ImageBean(imgPath));
    }

}

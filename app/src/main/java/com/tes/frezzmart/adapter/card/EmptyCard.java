package com.tes.frezzmart.adapter.card;

import androidx.annotation.DrawableRes;

import com.tes.frezzmart.R;
import com.tes.frezzmart.adapter.BaseCard;


public class EmptyCard extends BaseCard {


    public String hint;
    public @DrawableRes
    int backgroundRes = R.color.white;

    public EmptyCard() {
    }

    public EmptyCard(String hint) {
        this();
        this.hint = hint;
    }

    public EmptyCard(String hint, int backgroundRes) {
        this(hint);
        this.backgroundRes = backgroundRes;
    }
}

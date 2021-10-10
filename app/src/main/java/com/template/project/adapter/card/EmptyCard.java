package com.template.project.adapter.card;

import androidx.annotation.DrawableRes;

import com.template.project.R;
import com.template.project.adapter.BaseCard;


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

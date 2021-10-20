package com.template.android.adapter.card;

import android.content.res.ColorStateList;
import android.view.Gravity;

import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;

import com.template.android.R;


public class LabelCardMarque extends EditCard {

    public LabelCardMarque(CharSequence text) {
        this.text = text;
        this.skipCollect = true;
    }

    public String hint;
    public int textColorHint = 0xff999999;
    public int lines;
    public CharSequence text;
    public float lineSpacingExtra = 1f;  //lineSpacingExtra
    public int textColor = 0xff999999;
    public ColorStateList textColorState;
    public float textSize = 14;
    public int gravity = Gravity.START | Gravity.TOP;
    public int layout_gravity = gravity;

    public boolean boldTextWhileNormalHint;
    //    public boolean paddingTextView = true; //默认padding数据作用于textview控件  false 则padding数据作用于textView所在容器类 itemview
    public int textViewWidth = -2;

    public @DrawableRes
    int backgroundRes;
    public @DrawableRes
    int textViewBgRes;
    public Padding textViewPadding;

    public @DrawableRes
    int leftIcon;
    public @DrawableRes
    int rightIcon;

    public int drawablePadding;

    public @FontRes
    int fontResId = R.font.helvetica;

    public int selectId = -1;
    public boolean selected = false;

    @Override
    public Object getValue() {
        return this.selectId == -1 ? this.text : this.selectId;
    }
}

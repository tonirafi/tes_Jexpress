package com.template.project.adapter.card;

import androidx.annotation.DrawableRes;

import com.template.project.adapter.BaseCard;

import java.util.List;


/**
 * Created by 5Mall<zhangwei> on 2019-07-12
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class VPListCard extends BaseCard {

    public List<BaseCard> cards;
    public float itemRatio;
    public float screenRatio;
    public float multiScreenRatio;
    public boolean enableInfiniteLoop;
    public boolean keepCenter = true;

    public @DrawableRes
    int backgroundRes;
    public int itemSpacing;

    private VPListCard() {
        sort = 110;
    }

    private VPListCard(Builder builder) {
        this();
        this.cards = builder.cards;
        this.itemRatio = builder.itemRatio;
        this.multiScreenRatio = builder.multiScreenRatio;
        this.enableInfiniteLoop = builder.enableInfiniteLoop;
        this.backgroundRes = builder.backgroundRes;
        this.itemSpacing = builder.itemSpacing;
        this.keepCenter = builder.keepCenter;

        if (builder.screenRatio < 0) {
            this.screenRatio = builder.itemRatio / builder.multiScreenRatio;
        } else {
            this.screenRatio = builder.screenRatio;
        }
    }

    public static class Builder {
        private List<BaseCard> cards;
        private float itemRatio = 1f;
        private float screenRatio = -1f;
        private float multiScreenRatio = 0.716f;
        private boolean enableInfiniteLoop = true;
        private boolean keepCenter = true;

        private @DrawableRes
        int backgroundRes;
        private int itemSpacing;

        public Builder(List<BaseCard> cards) {
            this.cards = cards;
        }

        public Builder itemRatio(float itemRatio) {
            if (itemRatio <= 0)
                throw new IllegalArgumentException("itemRatio can not be negative");

            this.itemRatio = itemRatio;
            return this;
        }

        public Builder screenRatio(float screenRatio) {
            this.screenRatio = screenRatio;
            return this;
        }

        public Builder multiScreenRatio(float multiScreenRatio) {
            if (multiScreenRatio <= 0)
                throw new IllegalArgumentException("multiScreenRatio can not be negative");

            this.multiScreenRatio = multiScreenRatio;
            return this;
        }

        public Builder enableInfiniteLoop(boolean enableInfiniteLoop) {
            this.enableInfiniteLoop = enableInfiniteLoop;
            return this;
        }

        public Builder keepCenter(boolean keepCenter) {
            this.keepCenter = keepCenter;
            return this;
        }

        public Builder backgroundRes(@DrawableRes int backgroundRes) {
            this.backgroundRes = backgroundRes;
            return this;
        }

        public Builder itemSpacing(int itemSpacing) {
            this.itemSpacing = itemSpacing;
            return this;
        }

        public VPListCard build() {
            return new VPListCard(this);
        }

    }
}

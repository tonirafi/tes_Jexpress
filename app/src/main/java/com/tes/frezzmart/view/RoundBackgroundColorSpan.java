package com.tes.frezzmart.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import android.text.style.ReplacementSpan;

/**
 * Created by 5Mall<zhangwei> on 2019-06-03
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class RoundBackgroundColorSpan extends ReplacementSpan {

    private int textColor;

    private int bgColor;
    private float bgRadius;
    private float bgPaddingVertical;
    private float bgPaddingHorizontal;
    private float bgMargin;

    public float getBgPaddingHorizontal() {
        return bgPaddingHorizontal;
    }

    private RoundBackgroundColorSpan(Builder builder) {
        super();
        this.textColor = builder.textColor;
        this.bgColor = builder.bgColor;
        this.bgRadius = builder.bgRadius;
        this.bgPaddingVertical = builder.bgPaddingVertical;
        this.bgPaddingHorizontal = builder.bgPaddingHorizontal;
        this.bgMargin = builder.bgMargin;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) (paint.measureText(text, start, end) + bgPaddingHorizontal * 2 + bgMargin);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        int originColor = paint.getColor();
        if (textColor == 0) {
            this.textColor = originColor;
        }

        paint.setColor(this.bgColor);
        canvas.drawRoundRect(new RectF(x, top - bgPaddingVertical, x + getTextWidth(paint, text, start, end),
                y + paint.getFontMetrics().descent + bgPaddingVertical), bgRadius, bgRadius, paint);
        paint.setColor(this.textColor);
        canvas.drawText(text, start, end, x + bgPaddingHorizontal, y, paint);

        paint.setColor(originColor);
    }


    public float getTextWidth(@NonNull Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end) + bgPaddingHorizontal * 2;
    }

    public void resetMargin() {
        this.bgMargin = 0;
    }

    public float getBgMargin() {
        return this.bgMargin;
    }

    public static class Builder {
        private int textColor;

        private int bgColor;
        private float bgRadius;
        private float bgPaddingVertical;
        private float bgPaddingHorizontal;
        private float bgMargin = 20;

        public Builder(int bgColor) {
            this.bgColor = bgColor;
        }

        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

//        public Builder bgColor(int bgColor) {
//            this.bgColor = bgColor;
//            return this;
//        }

        public Builder bgRadius(float bgRadius) {
            this.bgRadius = bgRadius;
            return this;
        }

        public Builder bgPaddingVertical(float bgPaddingVertical) {
            this.bgPaddingVertical = bgPaddingVertical;
            return this;
        }

        public Builder bgPaddingHorizontal(float bgPaddingHorizontal) {
            this.bgPaddingHorizontal = bgPaddingHorizontal;
            this.bgMargin = bgPaddingHorizontal;
            return this;
        }

        public Builder bgMargin(float bgMargin) {
            this.bgMargin = bgMargin;
            return this;
        }

        public RoundBackgroundColorSpan build() {
            return new RoundBackgroundColorSpan(this);
        }
    }

}

package com.template.project.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.template.project.utils.DensityUtil;
import com.template.project.utils.LogUtil;


public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int dividerWidth;
    private int dividerWidthTop;
    private int dividerWidthBot;

    private Paint dividerPaint;

    boolean topAndBottomExclude = false; //上下无分割线

    public GridSpacingItemDecoration(int spanCount, int dividerWidthDp) {
        this(spanCount, dividerWidthDp, false);
    }

    /**
     * @param spanCount           gridLayoutManager 列数
     * @param dividerWidthDp      分割块宽高,单位:dp
     * @param topAndBottomExclude 上下无分割线
     */
    public GridSpacingItemDecoration(int spanCount, int dividerWidthDp, boolean topAndBottomExclude) {
        this.spanCount = spanCount;

        this.dividerPaint = new Paint();
        this.dividerPaint.setColor(Color.BLUE);

        this.dividerWidth = DensityUtil.dp2px(dividerWidthDp);
        this.dividerWidthTop = topAndBottomExclude ? 0 : dividerWidth / 2;
        this.dividerWidthBot = topAndBottomExclude ? 0 :dividerWidth - dividerWidthTop;
        this.topAndBottomExclude = topAndBottomExclude;

    }

    @Override
    public void getItemOffsets(Rect outRect, View child, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, child, parent, state);

        int pos = parent.getChildAdapterPosition(child);
        int column = (pos) % spanCount;// 计算这个child 处于第几列


        outRect.top = dividerWidthTop;
        outRect.bottom = dividerWidthBot;

        outRect.left = (column * dividerWidth / spanCount);
        outRect.right = dividerWidth - (column + 1) * dividerWidth / spanCount;

        LogUtil.logE("getItemOffsets", "pos=" + pos + ", column=" + column + " , left=" + outRect.left + ", right="
                + outRect.right + ", dividerWidth=" + dividerWidth);
    }

}


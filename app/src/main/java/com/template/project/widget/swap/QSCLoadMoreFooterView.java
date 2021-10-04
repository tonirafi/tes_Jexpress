package com.template.project.widget.swap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.template.project.R;

/**
 * Created by wsl on 16-7-20.
 */
public class QSCLoadMoreFooterView extends View implements SwipeTrigger, SwipeLoadMoreTrigger {

    private Paint linePaint;
    private Paint textPaint;
    private String text;

    public final String release_to_load;
    public final String pull_up_to_load;
    public final String load_completed;
    public final String loading;

    public QSCLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public QSCLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QSCLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        release_to_load = getResources().getString(R.string.release_to_load);
        pull_up_to_load = getResources().getString(R.string.pull_up_to_load);
        load_completed = getResources().getString(R.string.load_completed);
        loading = getResources().getString(R.string.app_loading);

        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xffaaaaaa);
        textPaint.setTextSize(sp2px(12));
        textPaint.setTextAlign(Paint.Align.CENTER);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(0xff888888);

    }

    @Override
    public void onLoadMore() {
        setText(loading);
    }

    @Override
    public void onPrepare() {
        linePaint.setColor(0xff888888);
        setText(pull_up_to_load);
    }

    //上拉加载／释放加载／加载中
    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled <= -getHeight()) {
                setText(release_to_load);
            } else {
                setText(pull_up_to_load);
            }
        } else {
            setText(load_completed);
        }
    }

    @Override
    public void onRelease() {
//        setText(loading);
    }

    @Override
    public void onComplete() {
        linePaint.setColor(0x00888888);
        setText(load_completed);
    }

    @Override
    public void onReset() {
        linePaint.setColor(0xff888888);
        setText(pull_up_to_load);
    }

    private void setText(String text) {
        this.text = text;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = getSuggestedMinimumWidth();
        int height = getSuggestedMinimumHeight();
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawLine(0, 0, getWidth(), 0, linePaint);

        float x = getWidth() / 2;
        float y = (getHeight() - (textPaint.descent() + textPaint.ascent())) / 2;
        canvas.drawText(this.text, x, y, textPaint);
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected float sp2px(float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }
}
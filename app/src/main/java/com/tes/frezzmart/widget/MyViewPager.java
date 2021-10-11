package com.tes.frezzmart.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import com.tmall.ultraviewpager.UltraViewPager;

import java.lang.reflect.Field;

public class MyViewPager extends UltraViewPager {

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollSpeed(int speed) {
        ViewPager viewPager = getViewPager();
        if (viewPager == null) return;

        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            // AccelerateInterpolator 是匀加速插值器
            MyScroller viewPagerScroller = new MyScroller(viewPager.getContext(), new AccelerateInterpolator());
            viewPagerScroller.setmDuration(speed);

            field.setAccessible(true);
            field.set(viewPager, viewPagerScroller);


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private float downX;    //按下时 的X坐标
    private float downY;    //按下时 的Y坐标

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canScrollHorizontally(-1) || canScrollHorizontally(1)) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = x;
                    downY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    //获取到距离差
                    float dx = x - downX;
                    float dy = y - downY;
                    if (Math.abs(dx) > Math.abs(dy)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }


        }
        return super.onTouchEvent(event);
    }


    public static class MyScroller extends Scroller {

        private int mDura;

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public MyScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        public int getmDuration() {
            return mDura;
        }

        public void setmDuration(int mDuration) {
            this.mDura = mDuration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            //super.startScroll(startX, startY, dx, dy);
            super.startScroll(startX, startY, dx, dy, mDura);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // super.startScroll(startX, startY, dx, dy, duration);
            super.startScroll(startX, startY, dx, dy, mDura);
        }
    }
}

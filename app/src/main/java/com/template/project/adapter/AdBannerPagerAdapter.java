package com.template.project.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.template.project.http.bean.AdBean;
import com.template.project.utils.DensityUtil;
import com.template.project.utils.PicassoUtil;
import com.template.project.widget.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class AdBannerPagerAdapter extends PagerAdapter {

    List<AdBean> mCards = new ArrayList<>();
    final float defaultRadius;

    public AdBannerPagerAdapter() {
        defaultRadius = DensityUtil.dp2Px(6);
    }

    public void clear() {
        this.mCards.clear();
    }

    public void addAll(List<AdBean> mCards) {
        this.mCards.addAll(mCards);
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        RoundedImageView contentView = new RoundedImageView(container.getContext());
        contentView.setScaleType(ImageView.ScaleType.FIT_XY);
        contentView.setCornerRadius(defaultRadius);
        contentView.setDefaultAspectRatio(0.5f);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        PicassoUtil.load(mCards.get(position).getCoverImage(), true).centerCrop().fit().into(contentView);

        contentView.setOnClickListener(v -> {
            if (onBannerClickListener != null) {
                onBannerClickListener.onBannerClick(position);
            }
        });
        container.addView(contentView);
        return contentView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ImageView cacheView = (ImageView) object;
        container.removeView(cacheView);
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    private OnBannerClickListener onBannerClickListener;

    public interface OnBannerClickListener {
        void onBannerClick(int position);
    }
}

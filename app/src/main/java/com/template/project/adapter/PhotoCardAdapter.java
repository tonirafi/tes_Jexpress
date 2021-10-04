package com.template.project.adapter;

import android.content.Context;

import androidx.annotation.DrawableRes;


import com.template.project.R;
import com.template.project.adapter.card.UploadImageUnitCard;
import com.template.project.adapter.providers.UploadImageUnitCardProvider;

import java.util.List;

public class PhotoCardAdapter extends CardAdapter {

    public PhotoCardAdapter(Context context) {
        super(context);
        extraCard = new UploadImageUnitCard(true);
    }

    UploadImageUnitCard extraCard;

    public void setPlaceholderDrawableRes(@DrawableRes int resId) {
        if (extraCard != null) {
            extraCard.placeholderDrawableRes = resId;
        }
    }

    /**
     * 默认最多可上传8张图片
     */
    private int maxNum = 8;
    public boolean showCoverTag = true;

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        notifyDataSetChanged();
    }

    public boolean isShowCoverTag() {
        return showCoverTag;
    }

    public void setShowCoverTag(boolean showCoverTag) {
        this.showCoverTag = showCoverTag;
        updateCoverState();
        notifyDataSetChanged();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener l) {
        super.setOnItemClickListener(l);
        setWithoutProvider(new UploadImageUnitCardProvider(l));
    }

    @Override
    public int getItemCount() {
        if (mList == null || mList.isEmpty()) return 1;

        return mList.size() >= maxNum ? mList.size() : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList == null || position == mList.size()) {
            return mCardPool.size() + 1;
        }
//        return mCardPool.indexOf(mList.get(position).getClass().getName());
        return super.getItemViewType(position);
    }

    public BaseCard getItem(int position) {
        if (position >= 0 && position <= mList.size() - 1) {
            return mList.get(position);
        }

        return extraCard;
    }

    @Override
    public void addAll(List list) {
        this.mList = list;
        updateCoverState();
    }

    private void updateCoverState() {
        if (this.mList != null && !this.mList.isEmpty()) {
            BaseCard card = this.mList.get(0);
            if (card instanceof UploadImageUnitCard) {
                ((UploadImageUnitCard) card).successText = showCoverTag ? "cover" : null;
            }
        }
    }

}

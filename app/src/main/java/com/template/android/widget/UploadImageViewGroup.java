package com.template.android.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.template.android.adapter.PhotoCardAdapter;
import com.template.android.adapter.card.UploadImageUnitCard;
import com.template.android.adapter.providers.UploadImageUnitCardProvider;
import com.template.android.router.IntentUtil;
import com.template.android.utils.upload.ImageUploadManager;
import com.template.android.utils.upload.image.bean.ImageBean;
import com.template.android.view.GridSpacingItemDecoration;
import com.walkermanx.photopicker.PhotoPreview;

import java.util.ArrayList;
import java.util.List;


public class UploadImageViewGroup extends LinearLayout implements UploadImageUnitCardProvider.UploadImageUnitImageListener {

    public UploadImageViewGroup(Context context) {
        this(context, null);
    }

    public UploadImageViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadImageViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UploadImageViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private RecyclerView mRv;
    private PhotoCardAdapter adapter;
    private List<UploadImageUnitCard> imageCards = new ArrayList<>(8);
    private int spanCount = 4;
    private int RSC_CODE_PHOTO = 300;

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mRv = new RecyclerView(context);
        mRv.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        this.addView(mRv, new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        GridLayoutManager gridLayoutManager = new InnerGridLayoutManager(getContext(), spanCount);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        mRv.setLayoutManager(gridLayoutManager);
        mRv.addItemDecoration(new GridSpacingItemDecoration(spanCount, 5));
        mRv.setHasFixedSize(true);
        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.setHasFixedSize(true);
        mRv.setNestedScrollingEnabled(false);
        adapter = new PhotoCardAdapter(getContext());
        adapter.setOnItemClickListener(this);
        mRv.setAdapter(adapter);
    }

    public static class InnerGridLayoutManager extends GridLayoutManager {
        public InnerGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public InnerGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public InnerGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        mRv.setNestedScrollingEnabled(enabled);
    }

    public void setPhotoRequestCode(int RequestCode) {
        this.RSC_CODE_PHOTO = RequestCode;
    }

    @Override
    public void preview(int position, ImageBean imageBean) {
        if (getContext() instanceof Activity) {
            PhotoPreview.builder()
                    //附带已经选中过的图片
                    .setPhotos(getSelectedPhotos())
                    //设置要浏览图片的第position张
                    .setCurrentItem(position)
//                    .setThemeColors(R.color.black,R.color.black,R.color.white)
                    .setShowToolbar(false)
                    .start((Activity) getContext());
        }

    }

    @Override
    public void remove(int position, ImageBean imageBean) {
        synchronized (UploadImageViewGroup.class) {
            if (imageBean.uploading()) {
                ImageUploadManager.getInstance(getContext()).cancelUploadImage(imageBean);
            }

            if (position < 0 || position > this.imageCards.size() - 1) return;

            requestFocus();
            this.imageCards.remove(position);
            adapter.addAll(imageCards);
            if ((imageCards.size() + 1) % spanCount == 0)
                adapter.notifyDataSetChanged();
            else {
                adapter.notifyItemRemoved(position);
                if (position == 0) {
                    adapter.notifyItemChanged(position);
                }
            }

        }
    }

    int selectPhotoActionCount = 0;

    @Override
    public void selectPhoto(int position) {
        if (selectPhotoInterceptor != null && selectPhotoInterceptor.doIntercept(selectPhotoActionCount++ == 0))
            return;

        if (getContext() instanceof Activity) {
            IntentUtil.intentToUri(getContext(), String.format("photo:///%s", RSC_CODE_PHOTO));
        }
    }

    @Override
    public void onItemOnclick(int position) {
        selectPhoto(position);
    }

    SelectPhotoInterceptor selectPhotoInterceptor;

    public interface SelectPhotoInterceptor {
        boolean doIntercept(boolean isFirst);
    }

    public void setSelectPhotoInterceptor(SelectPhotoInterceptor selectPhotoInterceptor) {
        this.selectPhotoInterceptor = selectPhotoInterceptor;
    }

    public void setPlaceholderDrawableRes(@DrawableRes int resId) {
        adapter.setPlaceholderDrawableRes(resId);
    }

    public List<UploadImageUnitCard> getCards() {
        return imageCards;
    }

    public ArrayList<String> getSelectedPhotos() {
        ArrayList<String> list = new ArrayList<>(8);
        for (UploadImageUnitCard card : this.imageCards) {
            if (card.isAddCard || card.imageBean == null || card.imageBean.isEmptyBean())
                continue;

            if (!TextUtils.isEmpty(card.imageBean.getImgPath())) {
                list.add(card.imageBean.getImgPath());
            } else if (!TextUtils.isEmpty(card.imageBean.getImageUrl())) {
                list.add(card.imageBean.getCompatibleImageUrl());
            }
        }

        return list;
    }

    public List<ImageBean> getUploadedImageBeans() {
        List<ImageBean> list = new ArrayList<>(8);
        for (UploadImageUnitCard card : this.imageCards) {
            if (card.isAddCard || card.imageBean == null)
                continue;

            if (card.imageBean.isUploadedLocalBean() || card.imageBean.isRemoteBean()) {
                list.add(card.imageBean);
            }
        }
        return list;
    }

    public List<ImageBean> getUploadingImageBeans() {
        List<ImageBean> list = new ArrayList<>(8);
        for (UploadImageUnitCard card : this.imageCards) {
            if (card.isAddCard || card.imageBean == null)
                continue;

            if (card.imageBean.uploading()) {
                list.add(card.imageBean);
            }
        }
        return list;
    }

    public List<ImageBean> getAllImageBeans() {
        List<ImageBean> list = new ArrayList<>(8);
        for (UploadImageUnitCard card : this.imageCards) {
            if (card.isAddCard || card.imageBean == null || card.imageBean.isEmptyBean())
                continue;

            list.add(card.imageBean);
        }
        return list;
    }

    public boolean isUploading() {
        for (UploadImageUnitCard card : this.imageCards) {
            if (card.isAddCard || card.imageBean == null)
                continue;

            if (card.imageBean.isNotYetUploadedLocalBean()) {
                return true;
            }
        }

        return false;
    }

    public void addAll(List<UploadImageUnitCard> cards) {
        if (cards == null || cards.isEmpty() || this.imageCards.size() >= getMaxNum())
            return;

        int delta = getMaxNum() - this.imageCards.size();
        this.imageCards.addAll(cards.size() > delta ? cards.subList(0, delta) : cards);
        adapter.addAll(imageCards);
        adapter.notifyDataSetChanged();
    }

    public void add(UploadImageUnitCard card) {
        if (card == null || this.imageCards.size() >= getMaxNum())
            return;

//        int delta = maxNum - this.imageCards.size();
        this.imageCards.add(card);
        adapter.addAll(imageCards);
        adapter.notifyDataSetChanged();
    }


    public void update(List<UploadImageUnitCard> imageCards) {

        requestFocus();

        if (imageCards == null || imageCards.isEmpty()) {
            this.imageCards.clear();
            adapter.notifyDataSetChanged();
            return;
        }

        int sum = imageCards.size() > getMaxNum() ? getMaxNum() : imageCards.size();
        List<UploadImageUnitCard> uploadCards = new ArrayList<>(sum);
        for (int i = 0; i < sum; i++) {
            uploadCards.add(transToCard(imageCards.get(i)));
        }

        this.imageCards = uploadCards;
        adapter.addAll(uploadCards);
        adapter.notifyDataSetChanged();

    }

    public void update(String... localImages) {
        requestFocus();
        if (localImages == null || localImages.length == 0) {
            this.imageCards.clear();
            adapter.notifyDataSetChanged();
            return;
        }

        int sum = localImages.length > getMaxNum() ? getMaxNum() : localImages.length;
        List<UploadImageUnitCard> imageCards = new ArrayList<>(sum);
        for (int i = 0; i < sum; i++) {
            imageCards.add(transToCard(localImages[i]));
        }

        this.imageCards = imageCards;
        adapter.addAll(imageCards);
        adapter.notifyDataSetChanged();
    }

    public void addAll(String... localImages) {
        if (localImages == null || localImages.length == 0) return;


        int sum = localImages.length > getMaxNum() ? getMaxNum() : localImages.length;
        List<UploadImageUnitCard> imageCards = new ArrayList<>(sum);
        for (int i = 0; i < sum; i++) {
            imageCards.add(transToCard(localImages[i]));
        }
        addAll(imageCards);
    }

    public int getMaxNum() {
        return adapter.getMaxNum();
    }

    public void setMaxNum(int maxNum) {
        this.adapter.setMaxNum(maxNum);
    }

    public boolean isShowCoverTag() {
        return this.adapter.isShowCoverTag();
    }

    public void setShowCoverTag(boolean showCoverTag) {
        this.adapter.setShowCoverTag(showCoverTag);
    }


    public void onDestroy() {
        ImageUploadManager.getInstance(getContext()).cancelUploadImage(getUploadingImageBeans());
    }


    private UploadImageUnitCard transToCard(@NonNull String imgPath) {

        for (UploadImageUnitCard card : imageCards) {
            if (card.imageBean != null) {
                if (imgPath.startsWith("http")) {
                    if (imgPath.equals(card.imageBean.getImageUrl()) && card.imageBean.isRemoteBean())
                        return card;
                } else if (imgPath.equals(card.imageBean.getImgPath())) {
                    return card;
                }
            }
        }

        return new UploadImageUnitCard(new ImageBean(imgPath));
    }


    private UploadImageUnitCard transToCard(@NonNull UploadImageUnitCard uploadImageUnitCard) {

        if (uploadImageUnitCard.imageBean == null || TextUtils.isEmpty(uploadImageUnitCard.imageBean.getImgPath()))
            return uploadImageUnitCard;

        for (UploadImageUnitCard card : imageCards) {
            if (card.imageBean != null && uploadImageUnitCard.imageBean.getImgPath().equals(card.imageBean.getImgPath())) {
                return card;
            }
        }

        return uploadImageUnitCard;
    }
}

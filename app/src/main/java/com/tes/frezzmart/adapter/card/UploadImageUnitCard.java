package com.tes.frezzmart.adapter.card;

import android.os.Parcel;
import android.os.Parcelable;

import com.tes.frezzmart.adapter.BaseCard;
import com.tes.frezzmart.utils.upload.image.bean.ImageBean;


public class UploadImageUnitCard extends BaseCard {

    public ImageBean imageBean;
    public boolean isAddCard = false;
    public int placeholderDrawableRes = -1;
    public String successText;

    public static final int IMAGE_NORMAL = 1;
    public static final int IMAGE_ADD = 2;
    public static final int VIDEO_NORMAL = 3;
    public static final int VIDEO_ADD = 4;

    public UploadImageUnitCard() {

    }

    public UploadImageUnitCard(boolean isAddCard) {
        this.isAddCard = isAddCard;
    }

    public UploadImageUnitCard(ImageBean imageBean) {
        this.imageBean = imageBean;
    }


    public UploadImageUnitCard(int sort) {
        this.sort = sort;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UploadImageUnitCard that = (UploadImageUnitCard) o;

        if (this.imageBean != null) return imageBean.equals(that.imageBean);

        return that.imageBean == null;
    }

    @Override
    public int hashCode() {
        return imageBean != null ? imageBean.hashCode() : 0;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.imageBean, flags);
    }

    protected UploadImageUnitCard(Parcel in) {
        super(in);
        this.imageBean = in.readParcelable(ImageBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<UploadImageUnitCard> CREATOR = new Parcelable.Creator<UploadImageUnitCard>() {
        @Override
        public UploadImageUnitCard createFromParcel(Parcel source) {
            return new UploadImageUnitCard(source);
        }

        @Override
        public UploadImageUnitCard[] newArray(int size) {
            return new UploadImageUnitCard[size];
        }
    };

}

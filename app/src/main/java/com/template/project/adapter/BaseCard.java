package com.template.project.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.template.project.AppConstants;
import com.template.project.http.bean.BaseBean;
import com.template.project.utils.DensityUtil;


/**
 * Created by 5Mall<zhangwei> on 2018/7/28
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class BaseCard extends BaseBean implements Comparable<BaseCard>, Parcelable {

    //默认水平/垂直 方向边距
    public static final int defaultPadding = DensityUtil.dp2px(16);

    public transient int sort;
    public String uri;
    public transient int uriRequestCode = AppConstants.RequestCode.DEFAULT_RSC_CODE;
    public transient boolean useUriRequestCodeAsItemId = false;
    public transient int cardId;
    public Padding padding;
    public Margin margin;
    public boolean loading;
    public boolean paddingFromLayout; //标记padding来源是否从布局中提取

    @Override
    public String toString() {
        return String.format("%s@%s", this.getClass().getName(), paddingFromLayout ? null : this.padding);
    }

    public BaseCard() {
    }

    protected BaseCard(Parcel in) {
        sort = in.readInt();
        uri = in.readString();
        cardId = in.readInt();
        padding = in.readParcelable(Padding.class.getClassLoader());
        margin = in.readParcelable(Margin.class.getClassLoader());
    }


    public static final Creator<BaseCard> CREATOR = new Creator<BaseCard>() {
        @Override
        public BaseCard createFromParcel(Parcel in) {
            return new BaseCard(in);
        }

        @Override
        public BaseCard[] newArray(int size) {
            return new BaseCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sort);
        dest.writeString(uri);
        dest.writeInt(cardId);
        dest.writeParcelable(padding, flags);
        dest.writeParcelable(margin, flags);
    }


    /**
     * 用于列表排序
     *
     * @param another
     * @return
     */
    @Override
    public int compareTo(@NonNull BaseCard another) {
        if (this.sort == another.sort) {
            return 0;
        }
        return this.sort > another.sort ? 1 : -1;
    }


    public BaseCard marginTop(int val) {
        this.margin = newMarginTop(val);
        return this;
    }

    public static Margin newMarginTop(int top) {
        return new Margin(0, top, 0, 0);
    }

    public static Margin newMarginBottom(int bottom) {
        return new Margin(0, 0, 0, bottom);
    }

    public static Margin newMargin() {
        return newMargin(0, 0, 0, 0);
    }


    public static Margin newMargin(int left, int top, int right, int bottom) {
        return new Margin(left, top, right, bottom);
    }

    public static Margin defaultHorizontalMargin() {
        return new Margin(defaultPadding, 0, defaultPadding, 0);
    }


    public static Padding newPadding() {
        return newPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
    }


    public static Padding newPaddingRaw() {
        return newPadding(0, 0, 0, 0);
    }

    public static Padding newPaddingHorizontal(int horizontal) {
        return newPadding(horizontal, defaultPadding, horizontal, defaultPadding);
    }

    public static Padding newPaddingHorizontalRaw(int horizontal) {
        return newPadding(horizontal, 0, horizontal, 0);
    }

    public static Padding newPaddingVertical(int top, int bottom) {
        return newPadding(defaultPadding, top, defaultPadding, bottom);
    }

    public static Padding newPaddingVerticalRaw(int top, int bottom) {
        return newPadding(0, top, 0, bottom);
    }


    public static Padding newPaddingVertical(int vertical) {
        return newPaddingVertical(vertical, vertical);
    }

    public static Padding newPaddingVerticalRaw(int vertical) {
        return newPaddingVerticalRaw(vertical, vertical);
    }


    public static Padding newPadding(int left, int top, int right, int bottom) {
        return new Padding(left, top, right, bottom);
    }

    public static class Padding extends BaseBean implements Parcelable {
        public int left;
        public int top;
        public int right;
        public int bottom;

        public Padding(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override
        public String toString() {
            return String.format("Padding-%s-%s-%s-%s", left, top, right, bottom);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.left);
            dest.writeInt(this.top);
            dest.writeInt(this.right);
            dest.writeInt(this.bottom);
        }

        protected Padding(Parcel in) {
            this.left = in.readInt();
            this.top = in.readInt();
            this.right = in.readInt();
            this.bottom = in.readInt();
        }

        public static final Creator<Padding> CREATOR = new Creator<Padding>() {
            @Override
            public Padding createFromParcel(Parcel source) {
                return new Padding(source);
            }

            @Override
            public Padding[] newArray(int size) {
                return new Padding[size];
            }
        };
    }


    public static class Margin extends BaseBean implements Parcelable {
        public int left;
        public int top;
        public int right;
        public int bottom;

        @Override
        public String toString() {
            return String.format("Margin-%s-%s-%s-%s", left, top, right, bottom);
        }

        public Margin(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public Margin(int left, int right) {
            this.left = left;
            this.top = 0;
            this.right = right;
            this.bottom = 0;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.left);
            dest.writeInt(this.top);
            dest.writeInt(this.right);
            dest.writeInt(this.bottom);
        }

        protected Margin(Parcel in) {
            this.left = in.readInt();
            this.top = in.readInt();
            this.right = in.readInt();
            this.bottom = in.readInt();
        }

        public static final Creator<Margin> CREATOR = new Creator<Margin>() {
            @Override
            public Margin createFromParcel(Parcel source) {
                return new Margin(source);
            }

            @Override
            public Margin[] newArray(int size) {
                return new Margin[size];
            }
        };
    }
}

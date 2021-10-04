package com.template.project.http.bean;

import com.google.gson.annotations.SerializedName;
import com.template.project.service.upload.image.bean.ImageBean;

/**
 * Created by 5Mall<zhangwei> on 2018/10/29
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class AdBean extends BaseBean {

    @SerializedName("image")
    public ImageBean cover;
    @SerializedName("url")
    public String link;
    @SerializedName("name")
    public String title;

    @SerializedName("title")
    public String fixedBannerTitle;
    private int type; //0 banner 1弹窗 2 Fixed Banner

    public AdBean(String cover) {
        this.cover = new ImageBean(cover);
    }

    public AdBean(int coverRes) {
        this.cover = new ImageBean("res:///" + coverRes);
    }

    public String getCoverImage() {
        if (cover == null) return null;
        final String imageUrl = cover.getImageUrl();
        return imageUrl == null || imageUrl.length() == 0 ? cover.getImgPath() : imageUrl;
    }

    public boolean isPopupAds() {
        return this.type == 1;
    }

    public boolean isNormalBanner() {
        return this.type == 0;
    }

    public boolean isFixedBanner() {
        return this.type == 2;
    }
}

package com.template.project.service.upload.image.bean;

import com.template.project.http.bean.BaseBean;

public class ImageCache extends BaseBean {

    public String imgPath;
    public String imageUrl;
    //    public int uploadStatus;

    public ImageCache() {

    }

    public ImageCache(String imgPath, String imageUrl) {
        this.imgPath = imgPath;
        this.imageUrl = imageUrl;
    }

    public ImageBean transToImageBean() {
        ImageBean imageBean = new ImageBean(imgPath);
        imageBean.setImageUrl(imageUrl);
//        imageBean.setUploadStatus(uploadStatus);
        return imageBean;
    }
}

package com.template.android.utils.upload;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.template.android.utils.upload.image.ImageUploader;
import com.template.android.utils.upload.image.ImageUploadingListener;
import com.template.android.utils.upload.image.bean.ImageBean;
import com.template.android.utils.upload.image.bean.UploadBean;

import java.util.List;


public class ImageUploadManager {

    private static volatile ImageUploadManager mInstance;
    private ImageUploader imageUploader;
    private SparseArray<UploadBean> requestList = new SparseArray<>(8);
//    private Context context;

    private ImageUploadManager(Context context) {
//        this.context = context.getApplicationContext();
        this.imageUploader = new ImageUploader(context.getApplicationContext());
    }

    public static ImageUploadManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ImageUploadManager.class) {
                if (mInstance == null) {
                    mInstance = new ImageUploadManager(context);
                }
            }
        }
        return mInstance;
    }

    public int uploadImage(ImageBean imageBean, ImageUploadingListener imgProcessListener) {

        if (imageBean == null || TextUtils.isEmpty(imageBean.getImgPath())) return 0;

//        if (imageUploader == null) {
//            imageUploader = new ImageUploader(context);
//        }

        UploadBean uploadBean = requestList.get(imageBean.getUuid());

        if (uploadBean == null) {
            uploadBean = new UploadBean(imageBean.getUuid(), imageBean.getImgPath(), imgProcessListener);
            uploadBean.setOptions(imageBean.getOptions());
            requestList.put(imageBean.getUuid(), uploadBean);
            //开始上传
            imageUploader.upload(uploadBean);
        } else {
            uploadBean.setImageUploadingListener(imgProcessListener);
            if (uploadBean.hasUploaded()) {
                imgProcessListener.onSuccess(uploadBean);
//            } else if (uploadBean.isUploading()) {
//                imgProcessListener.onProgress(uploadBean);
//            } else if (uploadBean.isFailed()) {
            } else {
                uploadBean.setOptions(imageBean.getOptions());
                imageUploader.upload(uploadBean);
            }
        }

        return uploadBean.getUuid();
    }

    public int[] uploadImage(List<ImageBean> imageBeans, ImageUploadingListener imgProcessListener) {
        int[] UUIDs = new int[imageBeans.size()];
        for (int i = 0; i < UUIDs.length; i++) {
            UUIDs[i] = uploadImage(imageBeans.get(0), imgProcessListener);
        }

        return UUIDs;
    }

    public void cancelUploadImage(List<ImageBean> imageBeans) {
        for (ImageBean imageBean : imageBeans) {
            cancelUploadImage(imageBean);
        }
    }

    public boolean cancelUploadImage(ImageBean imageBean) {
        if (imageBean == null || imageBean.getUuid() == 0) return false;

        return cancelUpload(imageBean.getUuid());
    }

    public boolean cancelUpload(int uuid) {
        UploadBean uploadBean = requestList.get(uuid);
        if (uploadBean == null)
            return false;

        return uploadBean.cancel();
    }

    public void cancelUpload(int... UUIDs) {
        for (int i = 0; i < UUIDs.length; i++) {
            cancelUpload(UUIDs[0]);
        }
    }

    public void cancelAllUpload() {
        for (int i = 0; i < requestList.size(); i++) {
            requestList.valueAt(i).cancel();
        }

    }

    public void removeAll() {
        cancelAllUpload();
        requestList.clear();
    }

}

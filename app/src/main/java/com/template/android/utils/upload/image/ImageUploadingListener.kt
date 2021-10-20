package com.template.android.utils.upload.image

import com.template.android.utils.upload.image.bean.UploadBean

/**
 *
 */
interface ImageUploadingListener {
    fun onSuccess(uploadBean: UploadBean)
    fun onFail(uploadBean: UploadBean)
    fun onProgress(uploadBean: UploadBean)
}

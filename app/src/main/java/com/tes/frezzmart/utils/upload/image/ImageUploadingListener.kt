package com.tes.frezzmart.utils.upload.image

import com.tes.frezzmart.utils.upload.image.bean.UploadBean

/**
 *
 */
interface ImageUploadingListener {
    fun onSuccess(uploadBean: UploadBean)
    fun onFail(uploadBean: UploadBean)
    fun onProgress(uploadBean: UploadBean)
}

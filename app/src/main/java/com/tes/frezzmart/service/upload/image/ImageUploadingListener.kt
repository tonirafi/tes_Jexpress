package com.tes.frezzmart.service.upload.image

import com.tes.frezzmart.service.upload.image.bean.UploadBean

/**
 *
 */
interface ImageUploadingListener {
    fun onSuccess(uploadBean: UploadBean)
    fun onFail(uploadBean: UploadBean)
    fun onProgress(uploadBean: UploadBean)
}

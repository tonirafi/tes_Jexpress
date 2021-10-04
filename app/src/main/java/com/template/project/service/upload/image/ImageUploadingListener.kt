package com.template.project.service.upload.image

import com.template.project.service.upload.image.bean.UploadBean

/**
 *
 */
interface ImageUploadingListener {
    fun onSuccess(uploadBean: UploadBean)
    fun onFail(uploadBean: UploadBean)
    fun onProgress(uploadBean: UploadBean)
}

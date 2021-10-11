package com.tes.frezzmart.service.upload.image.bean

import com.tes.frezzmart.service.upload.image.ImageUploadStatus
import com.tes.frezzmart.http.bean.BaseBean


/**
 * Created by kuangwen on 16/8/29.
 */
class UploadImageResult : BaseBean() {
    var uuid: String? = null
    var uploadStatus: Int = 0
    var url: String? = null
    var error: String? = null

    override fun toString() = "UploadImageResult{uuid='$uuid', uploadStatus=$uploadStatus, url=$url ,error=$error}"

    companion object {

        fun FAIL(uuid: String?, error: String?): UploadImageResult {
            val uploadImageResult = UploadImageResult()
            uploadImageResult.uuid = uuid
            uploadImageResult.error = error
            uploadImageResult.uploadStatus = ImageUploadStatus.FAILED.ordinal
            return uploadImageResult
        }

        fun SUCCESS(uuid: String?, url: String?): UploadImageResult {
            val uploadImageResult = UploadImageResult()
            uploadImageResult.uuid = uuid
            uploadImageResult.url = url
            uploadImageResult.uploadStatus = ImageUploadStatus.SUCCESS.ordinal
            return uploadImageResult
        }
    }

}

package com.template.project.service.upload.image.bean

import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.template.project.service.upload.image.ImageUploadStatus
import com.template.project.service.upload.image.ImageUploadingListener

class UploadBean(val uuid: Int, val imgPath: String, private var listener: ImageUploadingListener?) : UploadCallback {

    //    var imgPath: String? = null //要上传的本地图片路径
    var options: HashMap<String, Any>? = null

    var currSize: Long = 0   //当前上传大小
    var totalSize: Long = 0 //图片大小
    var uploadStatus = ImageUploadStatus.FAILED.ordinal
    var requestId: String? = null  //图片对应的上传id
    var errorInfo: String? = null  //图片对应的的错误信息
    var publicId: String? = null //image上传成功后返回的id 用于获取图片URL
    var originalImageUrl: String? = null //image上传成功后返回的原始图片URL
    var eagerImageUrl: String? = null //image上传成功后返回的加水印的图片URL
    var eagerThumbUrl: String? = null //image上传成功后返回的缩略图片URL


    fun getProgress() = if (totalSize > 0) String.format("%.1f", currSize.toFloat() / totalSize * 100) else "0.0"

    override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
        if (!canNotify(requestId)) return

//        val imgSuffix = if (imgPath.lastIndexOf('.') == -1) ".jpg" else imgPath.substring(imgPath.lastIndexOf('.'))
        this.requestId = requestId
        this.publicId = resultData?.get("public_id").toString()
        //"secure_url" -> "https://res.cloudinary.com/dqgl4hkkx/image/authenticated/s--TH8N5Oj1--/v1551932780/wsghmuyh0p5hhkrz2l8c.webp"
        this.originalImageUrl = resultData?.get("secure_url").toString()
        resultData?.get("eager")?.let { t ->
            if (t is List<*> && t.isNotEmpty()) {
                val eagers = t.map { it as Map<*, *> }
                this.eagerImageUrl = eagers.getOrNull(0)?.get("secure_url")?.toString()
                this.eagerThumbUrl = eagers.getOrNull(1)?.get("secure_url")?.toString()
            }
        }

        if (this.originalImageUrl.isNullOrEmpty()) {
            this.uploadStatus = ImageUploadStatus.FAILED.ordinal
            this.errorInfo = "can not fetch imageUrl "
            this.listener?.onFail(this)
        } else {
            this.uploadStatus = ImageUploadStatus.SUCCESS.ordinal
            this.listener?.onSuccess(this)
        }

    }

    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
        if (!canNotify(requestId)) return

        this.uploadStatus = ImageUploadStatus.UPLOADING.ordinal
        this.currSize = bytes
        this.totalSize = totalBytes
        this.listener?.onProgress(this)
    }

    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
        onStart(requestId)
        onProgress(requestId, currSize.takeIf { it > 0 } ?: 1, totalSize.takeIf { totalSize > 0 }
                ?: 100)
    }

    override fun onError(requestId: String?, error: ErrorInfo?) {
        onStart(requestId)
        if (!canNotify(requestId)) return

        this.uploadStatus = ImageUploadStatus.FAILED.ordinal
        this.errorInfo = error.toString()
        this.listener?.onFail(this)
        this.requestId = null
    }

    override fun onStart(requestId: String?) {
        if (this.requestId.isNullOrEmpty()) {
            this.requestId = requestId
        }
    }

    private fun canNotify(requestId: String?): Boolean = this.requestId != null && this.requestId == requestId

    fun isUploading(): Boolean = uploadStatus == ImageUploadStatus.UPLOADING.ordinal

    fun hasUploaded(): Boolean = uploadStatus == ImageUploadStatus.SUCCESS.ordinal

    fun isFailed(): Boolean = uploadStatus == ImageUploadStatus.FAILED.ordinal

    fun cancel(): Boolean {
        if (isUploading()) {
            setImageUploadingListener(null)
            MediaManager.get().unregisterCallback(this)
            MediaManager.get().cancelRequest(requestId)
            this.uploadStatus = ImageUploadStatus.FAILED.ordinal
            return true
        }

        return false
    }

    fun getImageUploadingListener(): ImageUploadingListener? = this.listener

    fun setImageUploadingListener(listener: ImageUploadingListener?) {
        this.listener = listener
    }
}
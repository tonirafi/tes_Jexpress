package com.tes.frezzmart.utils.upload.image.bean

import com.tes.frezzmart.http.bean.BaseBean


/**
 * Created by kuangwen on 16/8/29.
 */
class UploadImageProgress : BaseBean() {
    var path: String? = null
    var uuid: String? = null
    var currentSize: Long = 0
    var totalSize: Long = 0

    override fun toString() = "UploadImageProgress{uuid='$uuid', path=$path, currentSize=$currentSize ,totalSize=$totalSize}"

    companion object {

        private var progress: UploadImageProgress? = null
            get() {
                if (field == null) {
                    field = UploadImageProgress()
                }
                return field
            }


        fun INSTANCE(uuid: String?, imgPath: String?, bytes: Long, totalBytes: Long): UploadImageProgress {
            progress?.uuid = uuid
            progress?.path = imgPath
            progress?.currentSize = bytes
            progress?.totalSize = totalBytes
            return progress!!
        }
    }

    fun getProgress() = if (totalSize > 0) String.format("%.1f", currentSize.toFloat() / totalSize * 100) else "0.0"

    fun print() = "progress: [$currentSize/ $totalSize] =${getProgress()}%"
}

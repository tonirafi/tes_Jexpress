package com.tes.frezzmart.service.upload.image.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tes.frezzmart.service.upload.image.ImageUploadStatus
import com.tes.frezzmart.adapter.BaseCard
import com.tes.frezzmart.utils.LogUtil
import com.tes.frezzmart.utils.StringUtil
import java.io.File

/**
 *   Created by 5Mall<zhangwei> on 2018/8/13
 *   Email:zhangwei@qingsongchou.com
 *   描述：
 */
class ImageBean : BaseCard {

    @Transient
    var imgPath: String? = null //图片本地路径
    @SerializedName("image")
    var imageUrl: String? = null //image上传成功后返回的url

        set(value) {
            compatibleImageUrl = initVal
            compatibleImgThumb = initVal
            field = value
        }

    @SerializedName("thumb")
    var imgThumb: String? = null //缩略图的URL
        set(value) {
            compatibleImageUrl = initVal
            compatibleImgThumb = initVal
            field = value
        }

    @Transient
    private val initVal = "N/A"

    @Transient
    private var compatibleImageUrl: String = initVal //
    @Transient
    private var compatibleImgThumb: String = initVal //


    @Transient
    var currSize: Long = 0   //当前上传大小
    @Transient
    var totalSize: Long = 0 //图片大小
    @Transient
    var uploadStatus = ImageUploadStatus.INIT.ordinal  //0 success,1 failed ,2 uploading图片上传状态,默认是等待上传

    @Transient
    var uuid: Int = 0
    @Transient
    var flag: Int = 0 //多位置上传图片时，用于做标记

    constructor(imgPath: String?) {
        if (imgPath == null) return

        try {
            this.imgPath = imgPath
            this.uuid = "$imgPath-${File(imgPath).lastModified()}".hashCode()
        } catch (e: Exception) {
            this.uuid = imgPath.hashCode()
        }
    }

    constructor(imgPath: String, flag: Int) : this(imgPath) {
        this.flag = flag
    }

    constructor(parcel: Parcel) {
        imgPath = parcel.readString()
        currSize = parcel.readLong()
        totalSize = parcel.readLong()
        uploadStatus = parcel.readInt()
        imageUrl = parcel.readString()
        imgThumb = parcel.readString()
        flag = parcel.readInt()
        uuid = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel?, flag: Int) {
        dest?.writeString(imgPath)
        dest?.writeLong(currSize)
        dest?.writeLong(totalSize)
        dest?.writeInt(uploadStatus)
        dest?.writeString(imageUrl)
        dest?.writeString(imgThumb)
        dest?.writeInt(flag)
        dest?.writeInt(uuid)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ImageBean> {
        override fun createFromParcel(parcel: Parcel): ImageBean {
            return ImageBean(parcel)
        }

        override fun newArray(size: Int): Array<ImageBean?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "ImageBean{imgPath='$imgPath'" +
                ", currSize=$currSize" +
                ", totalSize=$totalSize" +
                ", uploadStatus=$uploadStatus" +
                ", imageUrl='$imageUrl'" +
                ", imgThumb='$imgThumb'" +
                ", flag=$flag" +
                ", uuid='$uuid'}"
    }

    fun update(uploadBean: UploadBean): Boolean = if (this.uuid == uploadBean.uuid) {
        this.uploadStatus = uploadBean.uploadStatus
        this.currSize = uploadBean.currSize
        this.totalSize = uploadBean.totalSize
        this.imageUrl = uploadBean.eagerImageUrl
        if (uploadBean.uploadStatus == ImageUploadStatus.SUCCESS.ordinal) {
            this.imgThumb = uploadBean.eagerThumbUrl
        }
        true
    } else false

    fun getProgress(): Float = if (totalSize > 0) currSize.toFloat() / totalSize * 100 else 0f

    fun reset() {
        this.uploadStatus = ImageUploadStatus.INIT.ordinal
    }

    fun needUpload(): Boolean = uploadStatus == ImageUploadStatus.INIT.ordinal
    fun uploading(): Boolean = uploadStatus == ImageUploadStatus.UPLOADING.ordinal
    private fun uploaded(): Boolean = uploadStatus == ImageUploadStatus.SUCCESS.ordinal
    fun failed(): Boolean = uploadStatus == ImageUploadStatus.FAILED.ordinal

    fun isRemoteBean() = !this.imageUrl.isNullOrEmpty() && this.imgPath.isNullOrEmpty()
    /**
     * 未上传成功的本地bean
     */
    fun isNotYetUploadedLocalBean() = this.imageUrl.isNullOrEmpty() && !this.imgPath.isNullOrEmpty()

    /**
     * 已经上传成功的本地bean
     */
    fun isUploadedLocalBean() = !this.imageUrl.isNullOrEmpty() && !this.imgPath.isNullOrEmpty()

    fun isEmptyBean() = this.imageUrl.isNullOrEmpty() && this.imgPath.isNullOrEmpty()

    fun transImageCache() = ImageCache(imgPath, imageUrl)

    fun getCompatibleThumb(): String? {
        if (compatibleImgThumb != initVal) {
            return compatibleImgThumb
        }
        LogUtil.logD("======getCompatibleThumb: \n")
        //兼容老版本项目中非authorized类型的图片地址
        compatibleImgThumb = if (StringUtil.isAuthenticatedImgUrl(imageUrl)) {
            LogUtil.logD("======[取thumb] AuthenticatedImgUrl: $imgThumb")
            imgThumb ?: ""
        } else {
            LogUtil.logD("======[取 origin] imageUrl: $imageUrl")
            imageUrl ?: ""
        }

        return compatibleImgThumb
    }

    fun getCompatibleImageUrl(): String? {
        if (compatibleImageUrl != initVal) {
            return compatibleImageUrl
        }

        LogUtil.logD("======getCompatibleImageUrl: \n")
        compatibleImageUrl = if (StringUtil.isAuthenticatedImgUrl(imageUrl)) {
            LogUtil.logD("======取 AuthenticatedImgUrl: $imageUrl")
            imageUrl ?: ""
        } else {
            LogUtil.logD("======加 formatUrlWthWatermark: $imageUrl")
            StringUtil.formatUrlWthWatermark(imageUrl)
        }

        LogUtil.logD("====== compatibleImageUrl: $compatibleImageUrl")
        //a_ignore,q_auto/c_scale,fl_relative,g_center,l_overlay_njgppi,w_0.9
        return compatibleImageUrl
    }

    //    var imgPath: String? = null //要上传的本地图片路径

    @delegate:Transient
    val options by lazy { HashMap<String, Any>(3) }
}
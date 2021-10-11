package com.tes.frezzmart.service.upload.image

import android.content.Context
import com.cloudinary.android.MediaManager
import com.cloudinary.android.preprocess.BitmapEncoder
import com.tes.frezzmart.service.upload.BitmapRotator
import com.tes.frezzmart.service.upload.MyImagePreprocessChain
import com.tes.frezzmart.service.upload.image.bean.UploadBean


/**
 *   Created by 5Mall<zhangwei> on 2018/8/13
 *   Email:zhangwei@qingsongchou.com
 *   描述：
 */
internal class ImageUploader(private val context: Context) {


    //android_upload_presets
//    fun upload(uploadBean: UploadBean, upload_preset: String) = upload(uploadBean, options.apply { put("upload_preset", upload_preset) })
//
//    fun upload(uploadBean: UploadBean): String = upload(uploadBean, "global_upload_presets")　¬

//    private val limit by lazy {
//        //根据当前设备屏幕 设置最大长宽
//        context.resources.displayMetrics.let {
//
//            if (it.heightPixels > 2000) {
//                return@let (2000 * it.widthPixels.toFloat() / it.heightPixels).toInt() to 2000
//            }
//
//            it.widthPixels to it.heightPixels
//
//        }
//    }

    fun upload(uploadBean: UploadBean): String {
        val options = uploadBean.options ?: HashMap()
        //        return uR.callback(uploadBean).dispatch()
        return MediaManager.get().upload(uploadBean.imgPath)
                .options(options.apply {
                    if (!contains("tags")) {
//                        put("tags", BuildConfig.CLOUDINARY_IMAGE_TAG)
                    }
                    if (!contains("upload_preset")) {
                        put("upload_preset", "global_upload_presets")
                    }
                })
//                .option("tags", BuildConfig.CLOUDINARY_IMAGE_TAG)
//                .option("eager", "c_scale,w_150") //此处 eager 不会覆盖upload_preset中定义的eager 上传成功会除了返回upload_preset中的eager产生的URL 还会返回此处的eager所变换URL
                // If you include preprocessing in your upload request, you also need to pass an
                // Android context instance when calling the dispatch method.
                .preprocess(
                    MyImagePreprocessChain.limitDimensionsChain(2000, 2000, BitmapRotator(uploadBean.imgPath))
                        .saveWith(BitmapEncoder(BitmapEncoder.Format.WEBP, 80))
                )
                .maxFileSize(2 * 1024 * 1024) //限制最大上传文件大小不超过2MB
                .callback(uploadBean)
                .dispatch(context)
    }

}
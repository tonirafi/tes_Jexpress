package com.tes.frezzmart.service.upload.image

import com.cloudinary.android.signed.Signature
import com.cloudinary.android.signed.SignatureProvider
import com.tes.frezzmart.service.upload.image.bean.CloudinaryConfig


/**
 *   Created by 5Mall<zhangwei> on 2018/8/15
 *   Email:zhangwei@qingsongchou.com
 *   描述：
 */
class CloudinarySignatureProvider : SignatureProvider {


    private fun getCloudinaryConfig(options: Map<String, Any?>?): CloudinaryConfig? {

//        try {
//            val response = ApiEngine.getInstance().apiService.getCloudinaryConfig(options).execute()
//            if (response.isSuccessful) {
//                val bR = response.body()
//                if (bR != null && bR.isResponseSuccessful)
//                    return bR.data
//            }
//        } catch (e: Exception) {
//        }

        return null
    }


    override fun provideSignature(options: MutableMap<Any?, Any?>?): Signature {
        val config = getCloudinaryConfig(options?.mapKeys { it.key.toString() })
                ?: throw RuntimeException("CloudinaryConfig can not be null !")

        return Signature(config.signature, config.apiKey, config.getTimestamp())
    }

    override fun getName() = "CloudinarySignatureProvider"
}
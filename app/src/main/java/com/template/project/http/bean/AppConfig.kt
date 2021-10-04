package com.template.project.http.bean

import com.google.gson.annotations.SerializedName

data class AppConfig(

	@field:SerializedName("versi_app")
	val versiApp: String? = null,

	@field:SerializedName("group_chat")
	val groupChat: String? = null,

	@field:SerializedName("biaya_topup")
	val biayaTopup: Int? = null,

	@field:SerializedName("ads_status")
	val adsStatus: Boolean? = null,

	@field:SerializedName("reward_ads")
	val reward_ads: Boolean? = false,

	@field:SerializedName("open_youtube")
	val openYoutube: Boolean? = null
)

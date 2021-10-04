package com.template.project.http.bean

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val data:T? = null,

	@field:SerializedName("message")
	val message: String? = null
)


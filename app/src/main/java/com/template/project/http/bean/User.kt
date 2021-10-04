package com.template.project.http.bean

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("account_activation_token")
	val accountActivationToken: String? = null,

	@field:SerializedName("no_hp")
	val noHp: String? = null,

	@field:SerializedName("type_user")
	val typeUser: String? = null,

	@field:SerializedName("password_reset_token")
	val passwordResetToken: String? = null,

	@field:SerializedName("auth_key")
	val authKey: String? = null,

	@field:SerializedName("saldo_view")
	val saldoView: String? = null,

	@field:SerializedName("saldo_iklan")
	val saldoIklan: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,


	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("total_iklan")
	val totalIklan: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

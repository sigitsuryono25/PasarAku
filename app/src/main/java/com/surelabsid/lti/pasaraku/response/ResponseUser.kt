package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseUser(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data_user")
	val dataUser: DataUser? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataUser(

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("tentang_anda")
	val tentangAnda: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("nomor_telepon")
	val nomorTelepon: String? = null,

	@field:SerializedName("nama_lengkap")
	val namaLengkap: String? = null,

	@field:SerializedName("google_token")
	val googleToken: String? = null,

	@field:SerializedName("facebook_token")
	val facebookToken: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("token")
	val token: String? = null
) : Parcelable

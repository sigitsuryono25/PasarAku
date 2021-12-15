package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseAppSettings(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("app_settings")
	val appSettings: AppSettings? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class AppSettings(

	@field:SerializedName("no_wa")
	val noWa: String? = null,

	@field:SerializedName("nama_perusahaan")
	val namaPerusahaan: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable

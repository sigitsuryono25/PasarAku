package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseProfileView(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("detail_profile")
	val detailProfile: DetailProfile? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DetailProfile(

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("iklan")
	val iklan: List<DataIklanItem?>? = null
) : Parcelable

@Parcelize
data class User(

	@field:SerializedName("bergabung")
	val bergabung: String? = null,

	@field:SerializedName("tentang_anda")
	val tentangAnda: String? = null,

	@field:SerializedName("nomor_telepon")
	val nomorTelepon: String? = null,

	@field:SerializedName("nama_lengkap")
	val namaLengkap: String? = null,

	@field:SerializedName("profile_pic")
	val profilePic: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("token")
	val token: String? = null
) : Parcelable


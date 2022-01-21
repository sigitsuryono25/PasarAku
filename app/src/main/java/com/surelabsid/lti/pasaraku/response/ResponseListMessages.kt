package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseListMessages(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data_message")
	val dataMessage: List<DataMessageItem?>? = null
) : Parcelable

@Parcelize
data class DataMessageItem(

	@field:SerializedName("incoming_msg_id")
	val incomingMsgId: String? = null,

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("nama_lengkap")
	val namaLengkap: String? = null,

	@field:SerializedName("facebook_token")
	val facebookToken: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("tentang_anda")
	val tentangAnda: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("nomor_telepon")
	val nomorTelepon: String? = null,

	@field:SerializedName("google_token")
	val googleToken: String? = null,

	@field:SerializedName("outgoing_msg_id")
	val outgoingMsgId: String? = null,

	@field:SerializedName("msg_id")
	val msgId: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
) : Parcelable

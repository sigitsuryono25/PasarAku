package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseChatHeader(

	@field:SerializedName("data")
	val data: List<ChatHeaderDataItem?>? = null
) : Parcelable

@Parcelize
data class ChatHeaderDataItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("added_by")
	val addedBy: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null,

	@field:SerializedName("token")
	val token: String? = null
) : Parcelable



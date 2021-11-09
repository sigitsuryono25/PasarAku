package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseSlider(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data_slider")
	val dataSlider: List<DataSliderItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataSliderItem(

	@field:SerializedName("cover")
	val cover: String? = null,

	@field:SerializedName("active")
	val active: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null
) : Parcelable

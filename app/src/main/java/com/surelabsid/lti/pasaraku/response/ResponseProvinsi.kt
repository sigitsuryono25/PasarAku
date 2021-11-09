package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseProvinsi(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data_provinsi")
	val dataProvinsi: List<DataProvinsiItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataProvinsiItem(

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("state")
	val state: String? = null
) : Parcelable

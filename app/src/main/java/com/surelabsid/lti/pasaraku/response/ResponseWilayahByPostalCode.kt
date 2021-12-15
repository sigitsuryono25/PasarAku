package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseWilayahByPostalCode(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data_wil")
	val dataWil: DataWil? = null
) : Parcelable

@Parcelize
data class Prov(

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("state")
	val state: String? = null
) : Parcelable

@Parcelize
data class DataWil(

	@field:SerializedName("kab")
	val kab: Kab? = null,

	@field:SerializedName("prov")
	val prov: Prov? = null
) : Parcelable

@Parcelize
data class Kab(

	@field:SerializedName("provinsi_id")
	val provinsiId: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("state")
	val state: String? = null
) : Parcelable

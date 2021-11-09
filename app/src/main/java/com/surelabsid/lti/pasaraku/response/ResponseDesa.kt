package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseDesa(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data_desa")
	val dataDesa: List<DataDesaItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataDesaItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("kecamatan_id")
	val kecamatanId: String? = null
) : Parcelable

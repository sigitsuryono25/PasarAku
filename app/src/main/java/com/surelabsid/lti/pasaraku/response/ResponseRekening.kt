package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseRekening(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data_rekening")
	val dataRekening: List<DataRekeningItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataRekeningItem(

	@field:SerializedName("bank")
	val bank: String? = null,

	@field:SerializedName("norek")
	val norek: String? = null,

	@field:SerializedName("pemilik")
	val pemilik: String? = null,

	@field:SerializedName("_id")
	val id: String? = null
) : Parcelable

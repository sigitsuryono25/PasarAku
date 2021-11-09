package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseKecamatan(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data_kecamatan")
	val dataKecamatan: List<DataKecamatanItem?>? = null
) : Parcelable

@Parcelize
data class DataKecamatanItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("kabupaten_id")
	val kabupatenId: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) : Parcelable

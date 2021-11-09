package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseSubKategori(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data_subkategori")
	val dataSubkategori: List<DataSubkategoriItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataSubkategoriItem(

	@field:SerializedName("id_kategori")
	val idKategori: String? = null,

	@field:SerializedName("id_sub")
	val idSub: String? = null,

	@field:SerializedName("nama_sub")
	val namaSub: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null
) : Parcelable

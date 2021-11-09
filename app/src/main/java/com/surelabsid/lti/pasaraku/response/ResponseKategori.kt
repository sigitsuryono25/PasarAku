package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseKategori(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data_kategori")
	val dataKategori: List<DataKategoriItem?>? = null
) : Parcelable

@Parcelize
data class DataKategoriItem(

	@field:SerializedName("id_kategori")
	val idKategori: String? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null,

	@field:SerializedName("nama_kategori")
	val namaKategori: String? = null
) : Parcelable

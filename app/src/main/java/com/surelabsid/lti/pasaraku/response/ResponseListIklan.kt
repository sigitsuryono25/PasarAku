package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseListIklan(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data_iklan")
	val dataIklan: List<DataIklanItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataIklanItem(

	@field:SerializedName("kondisi")
	val kondisi: String? = null,

	@field:SerializedName("iklan_id")
	val iklanId: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("lon")
	val lon: String? = null,

	@field:SerializedName("deskripsi_iklan")
	val deskripsiIklan: String? = null,

	@field:SerializedName("id_prov")
	val idProv: String? = null,

	@field:SerializedName("id_kab")
	val idKab: String? = null,

	@field:SerializedName("foto")
	val foto: List<String?>? = null,

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("added_by")
	val addedBy: String? = null,

	@field:SerializedName("judul_iklan")
	val judulIklan: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("id_kec")
	val idKec: String? = null
) : Parcelable

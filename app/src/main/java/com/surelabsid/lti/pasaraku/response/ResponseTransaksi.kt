package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseTransaksi(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data_trans")
	val dataTrans: List<DataTransItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataTransItem(

	@field:SerializedName("kondisi")
	val kondisi: String? = null,

	@field:SerializedName("iklan_id")
	val iklanId: String? = null,

	@field:SerializedName("lon")
	val lon: String? = null,

	@field:SerializedName("deskripsi_iklan")
	val deskripsiIklan: String? = null,

	@field:SerializedName("id_prov")
	val idProv: String? = null,

	@field:SerializedName("bank")
	val bank: String? = null,

	@field:SerializedName("expired")
	val expired: String? = null,

	@field:SerializedName("nominal")
	val nominal: String? = null,

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("added_by")
	val addedBy: String? = null,

	@field:SerializedName("judul_iklan")
	val judulIklan: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("durasi")
	val durasi: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null,

	@field:SerializedName("paket")
	val paket: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("status_premium")
	val statusPremium: String? = null,


	@field:SerializedName("start_at")
	val startAt: String? = null,

	@field:SerializedName("_id_ads")
	val idAds: String? = null,

	@field:SerializedName("is_accepted")
	val isAccepted: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id_kab")
	val idKab: String? = null,

	@field:SerializedName("is_premium")
	val isPremium: String? = null,

	@field:SerializedName("foto")
	val foto: List<String?>? = null,

	@field:SerializedName("rekening")
	val rekening: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: String? = null,

	@field:SerializedName("alasan_ditolak")
	val alasanDitolak: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("id_kec")
	val idKec: String? = null
) : Parcelable

package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponsePaket(

	@field:SerializedName("data_paket")
	val dataPaket: List<DataPaketItem?>? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataPaketItem(

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("id_paket")
	val idPaket: String? = null,

	@field:SerializedName("durasi")
	val durasi: String? = null
) : Parcelable

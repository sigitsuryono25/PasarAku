package com.surelabsid.lti.pasaraku.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseItemChat(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: List<ChatItem?>? = null
) : Parcelable

@Parcelize
data class ChatItem(

    @field:SerializedName("tipe_akun")
    val tipeAkun: String? = null,

    @field:SerializedName("no_telpon_kantor")
    val noTelponKantor: String? = null,

    @field:SerializedName("pass")
    val pass: String? = null,

    @field:SerializedName("nama_perusahaan")
    val namaPerusahaan: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("foto")
    val foto: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("added_by")
    val addedBy: String? = null,

    @field:SerializedName("web")
    val web: String? = null,

    @field:SerializedName("_id_item")
    val idItem: String? = null,

    @field:SerializedName("_id_chat")
    val idChat: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("no_telp")
    val noTelp: String? = null,

    @field:SerializedName("added_on")
    val addedOn: String? = null,

    @field:SerializedName("user_id_dest")
    val userIdDest: String? = null,

    @field:SerializedName("email")
    val email: String? = null
) : Parcelable

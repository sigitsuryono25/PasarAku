package com.surelabsid.lti.pasaraku.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigInteger

@Parcelize
data class PremiumModel(
    var id: String? = null,
    var _id_ads: String? = null,
    var nominal: Int? = null,
    var paket: String? = null,
    var durasi: String? = null,
    var is_accepted: String? = null,
    var bank: String? = null,
    var rekening: String? = null,
    var alasan_ditolak: String? = null,
    var added_on: String? = null
) : Parcelable
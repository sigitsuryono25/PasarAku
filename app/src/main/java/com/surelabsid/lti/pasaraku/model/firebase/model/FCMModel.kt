package com.surelabsid.lti.pasaraku.model.firebase.model

import com.google.gson.annotations.SerializedName

data class FCMModel(
    @SerializedName("to")
    var token: String? = null,

    @SerializedName("data")
    var body: MessageItem? = null
)
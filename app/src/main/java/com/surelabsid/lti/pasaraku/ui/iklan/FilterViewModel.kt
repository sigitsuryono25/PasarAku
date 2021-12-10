package com.surelabsid.lti.pasaraku.ui.iklan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    private val _kondisi = MutableLiveData<String>()
    val kondisi: LiveData<String> get() = _kondisi

    fun setKondisi(filter: String) {
        _kondisi.postValue(filter)
    }


}

data class Lokasi(
    var provinsi: String? = null,
    var kabupaten: String? = null,
    var kecamatan: String? = null,
)
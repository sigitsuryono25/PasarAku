package com.surelabsid.lti.pasaraku.ui.iklan

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.surelabsid.lti.base.BaseViewModel
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class IklanViewModel : BaseViewModel() {

    private val _data = SingleLiveEvent<GeneralResponse>()
    val data: LiveData<GeneralResponse> get() = _data


    fun sendIklan(
        mode: RequestBody,
        judulIklan: RequestBody,
        deskripsiIklan: RequestBody,
        lokasi: RequestBody,
        foto: MutableList<MultipartBody.Part?>?,
        harga: RequestBody,
        detail: RequestBody,
        kategori: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        added_by: RequestBody,
        id_kab: RequestBody?,
        id_kec: RequestBody?,
        id_prov: RequestBody?,
        kondisi: RequestBody,
        previousDelete: MutableList<RequestBody>,
        iklanId: RequestBody?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val api = api.sendIklan(
                    mode,
                    judulIklan,
                    deskripsiIklan,
                    lokasi,
                    foto,
                    harga,
                    detail,
                    kategori,
                    lat,
                    lon,
                    added_by,
                    id_kab,
                    id_kec,
                    id_prov,
                    kondisi,
                    previousDelete,
                    iklanId
                )
                _data.postValue(api)
            } catch (e: Throwable) {
                e.printStackTrace()
                _error.postValue(e)
            }
        }
    }
}
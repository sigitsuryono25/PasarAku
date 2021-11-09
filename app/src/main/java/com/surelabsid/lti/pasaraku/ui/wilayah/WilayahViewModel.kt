package com.surelabsid.lti.pasaraku.ui.wilayah

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.surelabsid.lti.base.BaseViewModel
import com.surelabsid.lti.pasaraku.response.ResponseProvinsi
import com.surelabsid.lti.pasaraku.response.ResponseSlider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WilayahViewModel: BaseViewModel() {

    private val _provinsi = MutableLiveData<ResponseProvinsi>()
    val provinsi: LiveData<ResponseProvinsi> get() = _provinsi

    fun getProvinsi(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val data = api.getProvinsi()
                _provinsi.postValue(data)
            }catch (t: Throwable){
                t.printStackTrace()
                _error.postValue(t)
            }
        }
    }
}
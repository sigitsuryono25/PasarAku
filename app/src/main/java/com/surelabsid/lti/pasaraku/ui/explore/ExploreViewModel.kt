package com.surelabsid.lti.pasaraku.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.surelabsid.lti.base.BaseViewModel
import com.surelabsid.lti.pasaraku.response.ResponseKategori
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.response.ResponseSlider
import com.surelabsid.lti.pasaraku.response.ResponseSubKategori
import com.surelabsid.lti.pasaraku.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExploreViewModel : BaseViewModel() {

    private val _kategoriList = MutableLiveData<ResponseKategori>()
    val kategoriList: LiveData<ResponseKategori> get() = _kategoriList

    private val _subKategori = MutableLiveData<ResponseSubKategori>()
    val subKategori: LiveData<ResponseSubKategori> get() = _subKategori

    private val _slider = MutableLiveData<ResponseSlider>()
    val slider: LiveData<ResponseSlider> get() = _slider

    private val _dataIklan = SingleLiveEvent<ResponseListIklan>()
    val dataIklan: LiveData<ResponseListIklan> get() = _dataIklan


    fun getKategori() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getKategoriList()
                _kategoriList.postValue(data)
            } catch (t: Throwable) {
                t.printStackTrace()
                _error.postValue(t)
            }
        }
    }

    fun getSlider() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getSlider()
                _slider.postValue(data)
            } catch (t: Throwable) {
                t.printStackTrace()
                _error.postValue(t)
            }
        }
    }

    fun getListIklan(page: String = "0", kategori: String? = null,
                     provinsi: String? = null, kabupaten: String? = null,
                     kecamatan: String? = null, userid: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val api = api.getListIklan(page, kategori, provinsi, kabupaten, kecamatan, userid)
                _dataIklan.postValue(api)
            } catch (t: Throwable) {
                t.printStackTrace()
                _error.postValue(t)
            }
        }
    }

    fun getSubKategori(idKategori: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getSubKategori(idKategori)
                _subKategori.postValue(data)
            } catch (t: Throwable) {
                _error.postValue(t)
            }
        }
    }

}
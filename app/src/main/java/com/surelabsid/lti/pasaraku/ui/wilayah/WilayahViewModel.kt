package com.surelabsid.lti.pasaraku.ui.wilayah

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.surelabsid.lti.base.BaseViewModel
import com.surelabsid.lti.pasaraku.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WilayahViewModel : BaseViewModel() {

    private val _provinsi = MutableLiveData<ResponseProvinsi>()
    val provinsi: LiveData<ResponseProvinsi> get() = _provinsi

    private val _kabupaten = MutableLiveData<ResponseKabupaten>()
    val kabupaten: LiveData<ResponseKabupaten> get() = _kabupaten

    private val _kecamatan = MutableLiveData<ResponseKecamatan>()
    val kecamatan: LiveData<ResponseKecamatan> get() = _kecamatan

    private val _selectedKab = MutableLiveData<DataKabupatenItem>()
    val selectedKab: LiveData<DataKabupatenItem> get() = _selectedKab

    private val _selectedKec = MutableLiveData<DataKecamatanItem>()
    val selectedKec: LiveData<DataKecamatanItem> get() = _selectedKec

    private val _fromSearch = MutableLiveData<Boolean>()
    val fromSearch : LiveData<Boolean> get() = _fromSearch


    fun setFromSearch(fromSearch: Boolean){
        _fromSearch.postValue(fromSearch)
    }

    fun getProvinsi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getProvinsi()
                _provinsi.postValue(data)
            } catch (t: Throwable) {
                t.printStackTrace()
                _error.postValue(t)
            }
        }
    }

    fun getKabupaten(idProvinsi: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getKabupaten(idProvinsi)
                _kabupaten.postValue(data)
            } catch (t: Throwable) {
                t.printStackTrace()
                _error.postValue(t)
            }
        }
    }

    fun getKecamatan(idKabupaten: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getKecamatan(idKabupaten)
                _kecamatan.postValue(data)
            } catch (t: Throwable) {
                t.printStackTrace()
                _error.postValue(t)
            }
        }
    }

    fun setSelectedKab(dataKabupatenItem: DataKabupatenItem?) {
        _selectedKab.postValue(dataKabupatenItem)
    }

    fun setSelectedKec(dataItemKecamatanItem: DataKecamatanItem?) {
        _selectedKec.postValue(dataItemKecamatanItem)
    }
}
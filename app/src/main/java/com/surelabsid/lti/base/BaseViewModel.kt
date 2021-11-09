package com.surelabsid.lti.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.surelabsid.lti.pasaraku.network.NetworkModule

open class BaseViewModel : ViewModel(){

    internal val api = NetworkModule.getService()

    internal val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error;


}
package com.surelabsid.lti.pasaraku.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.response.ResponseChatHeader
import com.surelabsid.lti.pasaraku.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class ChatHeaderViewModel : ViewModel() {
    private val mRoutes = NetworkModule.getService()
    val response = MutableLiveData<ResponseChatHeader>()
    val res = SingleLiveEvent<GeneralResponse>()
    val error = SingleLiveEvent<String>()


    fun getChatList(userId: String?, kind: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = mRoutes.getChatList(userId, kind)
                response.postValue(result)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                when (throwable) {
                    is HttpException -> error.postValue(throwable.message())
                    is IOException -> error.postValue(throwable.message)
                    else -> {
                        error.postValue("Unknown Error")
                    }
                }
            }
        }
    }
}
package com.surelabsid.lti.pasaraku.model.firebase.messaging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.surelabsid.lti.pasaraku.model.firebase.model.ChatHeader
import com.surelabsid.lti.pasaraku.model.firebase.model.FCMModel
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.response.ResponseItemChat
import com.surelabsid.lti.pasaraku.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.HttpException

class ChatViewModel : ViewModel() {
    private val mRoutes = NetworkModule.getService()
    val res = SingleLiveEvent<GeneralResponse>()
    val itemChat = MutableLiveData<ResponseItemChat>()
    val responseBody = MutableLiveData<ResponseBody>()
    val error = SingleLiveEvent<String>()


    fun insertChat(chatHeader: ChatHeader?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = mRoutes.insertChat(chatHeader)
                res.postValue(result)
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

    fun getItemChat(chatHeader: ChatHeader?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = mRoutes.getChatItem(chatHeader?._id.toString())
                itemChat.postValue(result)
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


    fun pushChat(fcmModel: FCMModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val api = NetworkModule.getFcmService().actionSendService(fcmModel)
                responseBody.postValue(api)
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
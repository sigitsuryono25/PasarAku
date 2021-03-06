package com.surelabsid.lti.pasaraku.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkModule {

    const val BASE_URL = "https://pasaraku.com/mobile/"
//    const val BASE_URL = "http://192.168.1.3/webproject/pasarAkuAdminCI3/"


    const val FIREBASE_URL = "https://fcm.googleapis.com/"

    private fun getOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL + "index.php/Api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttp())
            .build()
    }

    private fun getRetrofitNoApi(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttp())
            .build()
    }

    private fun getRetrofitFCM(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FIREBASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttp())
            .build()
    }

    fun getFcmService(): ApiService {
        return getRetrofitFCM().create(ApiService::class.java)
    }

    fun getService(): ApiService {
        return getRetrofit().create(ApiService::class.java)
    }

    fun getServiceNoApi(): ApiService {
        return getRetrofitNoApi().create(ApiService::class.java)
    }
}

package com.surelabsid.lti.pasaraku.network

import com.surelabsid.lti.pasaraku.response.ResponseKategori
import com.surelabsid.lti.pasaraku.response.ResponseProvinsi
import com.surelabsid.lti.pasaraku.response.ResponseSlider
import com.surelabsid.lti.pasaraku.response.ResponseSubKategori
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("ApiKategori/getKategoriList")
    suspend fun getKategoriList(): ResponseKategori

    @GET("ApiSubKategori/getSubKategoriList")
    suspend fun getSubKategori(@Query("id_kategori") idKategori: String): ResponseSubKategori

    @GET("ApiSlider/getSliderList")
    suspend fun getSlider(): ResponseSlider

    @GET("ApiProvinsi/getListProvinsi")
    suspend fun getProvinsi(): ResponseProvinsi

}

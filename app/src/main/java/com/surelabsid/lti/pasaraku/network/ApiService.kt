package com.surelabsid.lti.pasaraku.network

import com.surelabsid.lti.pasaraku.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET("ApiKategori/getKategoriList")
    suspend fun getKategoriList(): ResponseKategori

    @GET("ApiSubKategori/getSubKategoriList")
    suspend fun getSubKategori(@Query("id_kategori") idKategori: String): ResponseSubKategori

    @GET("ApiSlider/getSliderList")
    suspend fun getSlider(): ResponseSlider

    @GET("ApiProvinsi/getListProvinsi")
    suspend fun getProvinsi(): ResponseProvinsi

    @GET("ApiKabupaten/getListKabupaten")
    suspend fun getKabupaten(@Query("id_provinsi") idProvinsi: String?): ResponseKabupaten

    @GET("ApiKecamatan/getListKecamatan")
    suspend fun getKecamatan(@Query("id_kabupaten") idKabupaten: String?): ResponseKecamatan


    @Multipart
    @POST("ApiIklan/index")
    suspend fun sendIklan(
        @Part("judul_iklan") judulIklan: RequestBody,
        @Part("deskripsi_iklan") deskripsiIklan: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part foto: Array<MultipartBody.Part?>?,
        @Part("harga") harga: RequestBody,
        @Part("detail") detail: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("added_by") added_by: RequestBody,
        @Part("id_kab") id_kab: RequestBody,
        @Part("id_kec") id_kec: RequestBody,
        @Part("id_prov") id_prov: RequestBody,
        @Part("kondisi") kondisi: RequestBody,
    ): GeneralResponse


    @GET("ApiIklan/getListIklan")
    suspend fun getListIklan(@Query("page") page: String?, @Query("kategori") kategori: String? = null): ResponseListIklan
}

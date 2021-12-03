package com.surelabsid.lti.pasaraku.network

import com.surelabsid.lti.pasaraku.model.firebase.model.ChatHeader
import com.surelabsid.lti.pasaraku.model.firebase.model.FCMModel
import com.surelabsid.lti.pasaraku.model.UserRequest
import com.surelabsid.lti.pasaraku.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    //firebase
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAAHC5PL4w:APA91bFky0k98D3-M5XaAHMv0fEYYPsqzscnrh7HumspltJz5S_1V_ii9pECaNPgJc4ITU5Mu45Jl2UhYZRKAnHbJSPBeC0U4UQW8z1HSSiurX7CLJMiPHUDt1q3T6uLA_fdQkeD-r6x"
    )
    @POST("fcm/send")
    suspend fun actionSendService(@Body notificationService: FCMModel): ResponseBody

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


    @POST("ApiUser/index")
    suspend fun registerUser(@Body userRequest: UserRequest): GeneralResponse

    @FormUrlEncoded
    @POST("ApiUser/checkPhoneIfRegistered")
    suspend fun checkPhone(@Field("phone") phone: String): ResponseUser

    @FormUrlEncoded
    @POST("ApiUser/login")
    suspend fun login(
        @Field("phone") phone : String?,
        @Field("password") password: String?
    ): ResponseUser

    @FormUrlEncoded
    @POST("update-token")
    suspend fun updateToken(
        @Field("token") token: String?,
        @Field("_id_user") idUser: String?
    ): GeneralResponse

    @GET("chat-list")
    suspend fun getChatList(@Query("q") userId: String?): ResponseChatHeader

    @GET("chat-item")
    suspend fun getChatItem(@Query("q") _id: String?): ResponseItemChat

    @POST("insert-chat")
    suspend fun insertChat(@Body chatHeader: ChatHeader?): GeneralResponse

    @POST("delete-chat")
    suspend fun deleteChat(@Body chatHeader: ChatHeader?): GeneralResponse
}

package com.projectPAB.umkmkuningan.data.remote

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("v1/cost")
    suspend fun getShippingCost(
        @Field("api_key") apiKey: String,
        @Field("origin") origin: String,
        @Field("destination") destination: String,
        @Field("weight") weight: Int,
        @Field("courier") courier: String
    ): Response<OngkirResponse>

    @GET("wilayah/provinsi")
    suspend fun getProvinsi(
        @Query("api_key") apiKey: String
    ): Response<WilayahResponse>

    @GET("wilayah/kabupaten")
    suspend fun getKabupaten(
        @Query("api_key") apiKey: String,
        @Query("id_provinsi") idProvinsi: String
    ): Response<WilayahResponse>

    @GET("wilayah/kecamatan")
    suspend fun getKecamatan(
        @Query("api_key") apiKey: String,
        @Query("id_kabupaten") idKabupaten: String
    ): Response<WilayahResponse>
}

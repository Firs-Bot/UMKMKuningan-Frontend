package com.projectPAB.umkmkuningan.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL Backend Lokal (10.0.2.2 untuk emulator Android mengakses localhost PC)
    private const val BACKEND_BASE_URL = "http://192.168.1.8/UMKMKuningan-backend/"
    
    // URL API Ongkir (BinderByte)
    private const val SHIPPING_BASE_URL = "https://api.binderbyte.com/"

    val backendApiService: BackendApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApiService::class.java)
    }

    val shippingApiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SHIPPING_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

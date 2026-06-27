package com.projectPAB.umkmkuningan.data.remote

import com.projectPAB.umkmkuningan.domain.model.Product
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BackendApiService {
    
    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<BaseResponse<User>>

    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<BaseResponse<Any>>

    @GET("get_products.php")
    suspend fun getProducts(): Response<BaseResponse<List<Product>>>

    @GET("get_bookmarks.php")
    suspend fun getBookmarks(
        @Query("user_id") userId: Int
    ): Response<BaseResponse<List<Product>>>

    @FormUrlEncoded
    @POST("add_bookmark.php")
    suspend fun addBookmark(
        @Field("user_id") userId: Int,
        @Field("product_id") productId: Int
    ): Response<BaseResponse<Any>>

    @FormUrlEncoded
    @POST("remove_bookmark.php")
    suspend fun removeBookmark(
        @Field("user_id") userId: Int,
        @Field("product_id") productId: Int
    ): Response<BaseResponse<Any>>
}

data class BaseResponse<T>(
    val status: String,
    val message: String?,
    val data: T?,
    val user: T?
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String
)

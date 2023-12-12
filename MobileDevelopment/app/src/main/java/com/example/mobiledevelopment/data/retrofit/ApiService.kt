package com.example.mobiledevelopment.data.retrofit

import com.example.mobiledevelopment.data.response.DestinationResponse
import com.example.mobiledevelopment.data.response.DetailResponse
import com.example.mobiledevelopment.data.response.LoginResponse
import com.example.mobiledevelopment.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("main")
    suspend fun getStories(
        @Header("Authorization") auth: String,
        @Field("lat") lat: Double = -6.201164960437535,
        @Field("lon") lon: Double = 106.77238584716075
    ): Response<DestinationResponse>
}
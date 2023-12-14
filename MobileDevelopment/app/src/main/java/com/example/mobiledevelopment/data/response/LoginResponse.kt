package com.example.mobiledevelopment.data.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

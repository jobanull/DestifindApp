package com.example.mobiledevelopment.data.pref

import com.google.gson.annotations.SerializedName

data class LoginResult(

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("token")
    val token: String? = null,

    val category: String = "",

    val age: String = "",

    val isLogin: Boolean = false
)

package com.example.mobiledevelopment.data.response

import com.google.gson.annotations.SerializedName

data class AgeResponse(
    @field:SerializedName("age")
    val age: Int? = null,
)

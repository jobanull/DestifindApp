package com.example.mobiledevelopment.data.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @field:SerializedName("category")
    val category: String? = null,
)

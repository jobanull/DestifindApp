package com.example.mobiledevelopment.data.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class DestinationResponse(

    @field:SerializedName("destinations")
    val listDst: List<ListDestinationItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
@Parcelize
data class ListDestinationItem(

    @field:SerializedName("title")
    val name: String = "",

    @field:SerializedName("description")
    val description: String? = "",

    @field:SerializedName("imageUrl")
    val photoUrl: String = "",

    @field:SerializedName("rating")
    val rating: Double? = 0.0,

    @field:SerializedName("distance")
    val distance: String? = "",

    @field:SerializedName("estimatedTime")
    val estimatedTime: String? = "",

    @field:SerializedName("latitude")
    val lat: Double = 0.0,

    @field:SerializedName("longitude")
    val lon: Double = 0.0,
): Parcelable
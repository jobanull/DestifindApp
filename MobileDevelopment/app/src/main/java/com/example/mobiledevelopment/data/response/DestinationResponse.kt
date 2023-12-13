package com.example.mobiledevelopment.data.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class DestinationResponse(

    @field:SerializedName("placesList")
    val listStory: List<ListDestinationItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
@Parcelize
data class ListDestinationItem(

    @PrimaryKey
    @field:SerializedName("title")
    val name: String = "",

    @field:SerializedName("imageUrl")
    val photoUrl: String = "",

    @field:SerializedName("description")
    val description: String? = "",

//    @field:SerializedName("rating")
//    val rating: Double? = 0.0,
//
//    @field:SerializedName("distance")
//    val distance: Int? = 1,
//
//    @field:SerializedName("estimatedTime")
//    val estimatedTime: Int? = 1,

    @field:SerializedName("latitude")
    val lat: Double = 0.0,

    @field:SerializedName("longitude")
    val lon: Double = 0.0,
): Parcelable
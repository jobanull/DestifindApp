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
data class DetailResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("story")
    val story: ListDestinationItem? = null
)
@Parcelize
data class ListDestinationItem(

    @field:SerializedName("imageUrl")
    val photoUrl: String = "",

    @PrimaryKey
    @field:SerializedName("title")
    val name: String = "",

    @field:SerializedName("description")
    val description: String? = "",

    @field:SerializedName("longitude")
    val lon: Double = 0.0,


    @field:SerializedName("latitude")
    val lat: Double = 0.0
): Parcelable
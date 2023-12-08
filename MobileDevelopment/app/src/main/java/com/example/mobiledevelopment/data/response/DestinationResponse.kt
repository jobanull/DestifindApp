package com.example.mobiledevelopment.data.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class DestinationResponse(

    @field:SerializedName("listStory")
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

    @field:SerializedName("photoUrl")
    val photoUrl: String = "",

    @field:SerializedName("createdAt")
    val createdAt: String = "",

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("description")
    val description: String = "",

    @field:SerializedName("lon")
    val lon: Double = 0.0,

    @PrimaryKey
    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("lat")
    val lat: Double = 0.0
): Parcelable
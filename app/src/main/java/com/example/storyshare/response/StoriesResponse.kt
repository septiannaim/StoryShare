package com.example.storyshare.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @SerializedName("error")
    val hasError: Boolean,

    @SerializedName("message")
    val statusMessage: String? = null,

    @SerializedName("listStory")
    val stories: List<StoryDetails>
) {
    data class StoryDetails(
        @SerializedName("id")
        val storyId: String? = null,

        @SerializedName("name")
        val authorName: String? = null,

        @SerializedName("description")
        val storyDescription: String? = null,

        @SerializedName("photoUrl")
        val imageUrl: String? = null,

        @SerializedName("createdAt")
        val creationDate: String,

        @SerializedName("lat")
        val latitude: Double? = null,

        @SerializedName("lon")
        val longitude: Double? = null
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            requireNotNull(parcel.readString()), // Non-nullable field
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(storyId)
            parcel.writeString(authorName)
            parcel.writeString(storyDescription)
            parcel.writeString(imageUrl)
            parcel.writeString(creationDate)
            parcel.writeValue(latitude)
            parcel.writeValue(longitude)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<StoryDetails> {
            override fun createFromParcel(parcel: Parcel): StoryDetails = StoryDetails(parcel)
            override fun newArray(size: Int): Array<StoryDetails?> = arrayOfNulls(size)
        }
    }
}

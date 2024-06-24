package com.example.storyshare.response

import com.google.gson.annotations.SerializedName


data class LoginDetails(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("name")
    val userName: String,

    @SerializedName("token")
    val authToken: String
)
package com.example.storyshare.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("error")
    val hasError: Boolean,

    @SerializedName("message")
    val responseMessage: String
)

package com.example.storyshare.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val isError: Boolean,

    @SerializedName("message")
    val statusMessage: String,

    @SerializedName("loginResult")
    val loginDetails: LoginDetails
)



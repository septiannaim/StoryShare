package com.example.storyshare.response

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    val fullName: String? = null,

    @SerializedName("email")
    val emailAddress: String? = null,

    @SerializedName("password")
    val userPassword: String? = null
)
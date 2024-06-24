package com.example.storyshare.response

import com.google.gson.annotations.SerializedName

data class UploadStoryResponse (
    @SerializedName("error")
    val isError: Boolean? = null,

    @SerializedName("message")
    val statusMessage: String? = null
)

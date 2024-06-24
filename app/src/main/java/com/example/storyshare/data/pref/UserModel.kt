package com.example.storyshare.data.pref

data class UserModel(
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)
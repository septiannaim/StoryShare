package com.example.storyshare.utils

sealed class Outcome<out T> private constructor() {
    data class Success<out R>(val value: R): Outcome<R>()
    data class Failure(val message: String): Outcome<Nothing>()
    object InProgress: Outcome<Nothing>()
}
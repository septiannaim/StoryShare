package com.example.storyshare.view.signup

import androidx.lifecycle.ViewModel
import com.example.storyshare.data.UserRepository


class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun registerUser(name: String, email: String, password: String) =
        userRepository.registerUser(name, email, password)

}

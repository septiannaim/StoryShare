package com.example.storyshare.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyshare.data.UserRepository
import com.example.storyshare.data.pref.UserModel

class WelcomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserSession(): LiveData<UserModel> {
        return userRepository.fetchCurrentUserData()
    }
}

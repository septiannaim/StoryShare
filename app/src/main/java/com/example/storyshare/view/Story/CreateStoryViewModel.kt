package com.example.storyshare.view.Story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyshare.data.UserRepository
import com.example.storyshare.data.pref.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(private val repository: UserRepository) : ViewModel() {

    fun submitStory(token: String, file: MultipartBody.Part, description: RequestBody) =
        repository.uploadStory(token, file, description)

    fun retrieveUser(): LiveData<UserModel> {
        return repository.fetchCurrentUserData()
    }
}

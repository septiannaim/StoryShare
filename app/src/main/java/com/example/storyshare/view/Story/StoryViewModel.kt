package com.example.storyshare.view.Story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyshare.data.UserRepository
import com.example.storyshare.data.pref.UserModel
import com.example.storyshare.response.StoriesResponse

class StoryViewModel(private val repository: UserRepository) : ViewModel() {

    fun fetchStories(): LiveData<PagingData<StoriesResponse.StoryDetails>> {
        return repository.fetchAllStories().cachedIn(viewModelScope)
    }

    fun fetchCurrentUser(): LiveData<UserModel> {
        return repository.fetchCurrentUserData()
    }
}

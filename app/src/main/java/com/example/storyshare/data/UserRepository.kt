package com.example.storyshare.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyshare.api.ApiService
import com.example.storyshare.data.adapter.StoryPagingDataSource
import com.example.storyshare.data.pref.UserModel
import com.example.storyshare.data.pref.UserPreferences
import com.example.storyshare.response.LoginRequest
import com.example.storyshare.response.LoginResponse
import com.example.storyshare.response.RegisterRequest
import com.example.storyshare.response.RegisterResponse
import com.example.storyshare.response.StoriesResponse
import com.example.storyshare.response.UploadStoryResponse
import com.example.storyshare.utils.Outcome
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository(private val userPreferences: UserPreferences, private val apiService: ApiService) {

    fun loginUser(email: String, password: String): LiveData<Outcome<LoginResponse>> = liveData {
        emit(Outcome.InProgress)
        try {
            val response = apiService.loginUser(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Outcome.Success(it))
                } ?: emit(Outcome.Failure("Login response body is null"))
            } else {
                emit(Outcome.Failure("Login failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.d("Login Fail", e.message.toString())
            emit(Outcome.Failure(e.message.toString()))
        }
    }

    suspend fun saveUserSession(userModel: UserModel) {
        userPreferences.saveUserData(userModel)
    }

    suspend fun login() {
        userPreferences.login()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    fun registerUser(name: String, email: String, password: String): LiveData<Outcome<RegisterResponse>> = liveData {
        emit(Outcome.InProgress)
        try {
            val response = apiService.registerUser(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Outcome.Success(it))
                } ?: emit(Outcome.Failure("Register response body is null"))
            } else {
                emit(Outcome.Failure("Register failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(Outcome.Failure(e.message.toString()))
        }
    }

    fun fetchAllStories(): LiveData<PagingData<StoriesResponse.StoryDetails>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingDataSource(apiService, userPreferences)
            }
        ).liveData
    }

    fun fetchCurrentUserData(): LiveData<UserModel> {
        return userPreferences.getUserData().asLiveData()
    }

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<Outcome<UploadStoryResponse>> = liveData {
        emit(Outcome.InProgress)
        try {
            val response = apiService.uploadStory(token, file, description)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Outcome.Success(it))
                } ?: emit(Outcome.Failure("Upload story response body is null"))
            } else {
                emit(Outcome.Failure("Upload story failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.d("Story Upload Failed", e.message.toString())
            emit(Outcome.Failure(e.message.toString()))
        }
    }

    fun getAllStoryLocation(token: String): LiveData<Outcome<StoriesResponse>> = liveData {
        emit(Outcome.InProgress)
        try {
            val response = apiService.getStoriesWithLocation(token, 1)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Outcome.Success(it)) // Emitting the actual StoriesResponse directly
                } ?: emit(Outcome.Failure("Response body is null"))
            } else {
                emit(Outcome.Failure("Request failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
            emit(Outcome.Failure(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(userPreferences: UserPreferences, apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreferences, apiService).also { instance = it }
            }
    }
}

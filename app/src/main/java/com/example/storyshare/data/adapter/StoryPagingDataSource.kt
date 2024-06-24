package com.example.storyshare.data.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyshare.api.ApiService
import com.example.storyshare.data.pref.UserPreferences
import com.example.storyshare.response.StoriesResponse
import kotlinx.coroutines.flow.first

class StoryPagingDataSource(
    private val apiService: ApiService,
    private val pref: UserPreferences
) : PagingSource<Int, StoriesResponse.StoryDetails>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoriesResponse.StoryDetails> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer ${pref.getUserData().first().token}"
            val response = apiService.fetchAllStories(token, page, params.loadSize)

            if (response.isSuccessful) {
                val responseData = response.body()?.stories ?: emptyList()

                LoadResult.Page(
                    data = responseData,
                    prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                    nextKey = if (responseData.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error fetching stories: ${response.message()}"))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoriesResponse.StoryDetails>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}

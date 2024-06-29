package com.example.storyshare.view.Story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.example.storyshare.data.UserRepository
import com.example.storyshare.response.StoriesResponse
import com.example.storyshare.DataDummy
import com.example.storyshare.MainDispatcherRule
import com.example.storyshare.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: UserRepository = Mockito.mock(UserRepository::class.java)
    private val viewModel = StoryViewModel(repository)

    @Test
    fun `fetchStories when success`() = runBlockingTest {
        // Given
        val dummyStories = PagingData.from(DataDummy.generateDummyStories())
        val liveDataPaging = flowOf(dummyStories).asLiveData()

        Mockito.`when`(repository.fetchAllStories()).thenReturn(liveDataPaging)

        // When
        val result = viewModel.fetchStories().getOrAwaitValue()

        // Then
        assertNotNull(result)

        val differ = androidx.paging.AsyncPagingDataDiffer(
            diffCallback = object : androidx.recyclerview.widget.DiffUtil.ItemCallback<StoriesResponse.StoryDetails>() {
                override fun areItemsTheSame(oldItem: StoriesResponse.StoryDetails, newItem: StoriesResponse.StoryDetails): Boolean {
                    return oldItem.storyId == newItem.storyId
                }

                override fun areContentsTheSame(oldItem: StoriesResponse.StoryDetails, newItem: StoriesResponse.StoryDetails): Boolean {
                    return oldItem == newItem
                }
            },
            updateCallback = object : androidx.recyclerview.widget.ListUpdateCallback {
                override fun onInserted(position: Int, count: Int) {}
                override fun onRemoved(position: Int, count: Int) {}
                override fun onMoved(fromPosition: Int, toPosition: Int) {}
                override fun onChanged(position: Int, count: Int, payload: Any?) {}
            },
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher
        )
        differ.submitData(result)

        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Verify that the data is not null after submitting data
        assertNotNull(differ.snapshot())

        // Verify the number of items loaded
        assertEquals(DataDummy.generateDummyStories().size, differ.itemCount)

        // Verify the first item loaded
        assertEquals(DataDummy.generateDummyStories()[0], differ.snapshot()[0])
    }

    @Test
    fun `fetchStories when no data`() = runBlockingTest {
        // Given
        val emptyStories = PagingData.from(DataDummy.generateEmptyStories())
        val liveDataPaging = flowOf(emptyStories).asLiveData()

        Mockito.`when`(repository.fetchAllStories()).thenReturn(liveDataPaging)

        // When
        val result = viewModel.fetchStories().getOrAwaitValue()

        // Then
        assertNotNull(result)
        val differ = androidx.paging.AsyncPagingDataDiffer(
            diffCallback = object : androidx.recyclerview.widget.DiffUtil.ItemCallback<StoriesResponse.StoryDetails>() {
                override fun areItemsTheSame(oldItem: StoriesResponse.StoryDetails, newItem: StoriesResponse.StoryDetails): Boolean {
                    return oldItem.storyId == newItem.storyId
                }

                override fun areContentsTheSame(oldItem: StoriesResponse.StoryDetails, newItem: StoriesResponse.StoryDetails): Boolean {
                    return oldItem == newItem
                }
            },
            updateCallback = object : androidx.recyclerview.widget.ListUpdateCallback {
                override fun onInserted(position: Int, count: Int) {}
                override fun onRemoved(position: Int, count: Int) {}
                override fun onMoved(fromPosition: Int, toPosition: Int) {}
                override fun onChanged(position: Int, count: Int, payload: Any?) {}
            },
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher
        )
        differ.submitData(result)

        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Verify that the data is not null after submitting data
        assertNotNull(differ.snapshot())

        // Verify that no items are loaded
        assertEquals(0, differ.itemCount)
    }
}

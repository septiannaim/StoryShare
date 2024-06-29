package com.example.storyshare

import com.example.storyshare.response.StoriesResponse

object DataDummy {

    fun generateDummyStories(): List<StoriesResponse.StoryDetails> {
        val storyList = ArrayList<StoriesResponse.StoryDetails>()
        for (i in 0..5) {
            val story = StoriesResponse.StoryDetails(
                storyId = "id_$i",
                authorName = "Author $i",
                storyDescription = "Description $i",
                imageUrl = "https://example.com/photo_$i.png",
                creationDate = "2022-02-22T22:22:22Z",
                latitude = i.toDouble(),
                longitude = i.toDouble()
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateEmptyStories(): List<StoriesResponse.StoryDetails> {
        return emptyList()
    }
}

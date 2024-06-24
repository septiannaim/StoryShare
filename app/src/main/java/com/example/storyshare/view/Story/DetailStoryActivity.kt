package com.example.storyshare.view.Story


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyshare.databinding.ActivityDetailBinding
import com.example.storyshare.response.StoriesResponse

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story Detail"

        val storyData = intent.getParcelableExtra<StoriesResponse.StoryDetails>(EXTRA_STORY_DETAIL)
        storyData?.let {
            binding.storyNameDetail.text = it.authorName
            binding.storyDescriptionDetail.text = it.storyDescription

            Glide.with(this)
                .load(it.imageUrl)
                .apply(RequestOptions().override(70, 70))
                .into(binding.storyImageDetail)
        }
    }

    companion object {
        const val EXTRA_STORY_DETAIL = "extra_story_detail"
    }
}

package com.example.storyshare.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyshare.databinding.ItemRowStoryBinding
import com.example.storyshare.response.StoriesResponse
import com.example.storyshare.view.Story.DetailStoryActivity

class StoryPagingAdapter : PagingDataAdapter<StoriesResponse.StoryDetails, StoryPagingAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    inner class StoryViewHolder(private val binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        internal fun bind(story: StoriesResponse.StoryDetails) {
            Glide.with(itemView.context)
                .load(story.imageUrl)
                .apply(RequestOptions().override(70, 70))
                .into(binding.storyImage)
            binding.storyTitle.text = story.authorName
            binding.storyDescription.text = story.storyDescription

            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.EXTRA_STORY_DETAIL, story)
                }

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.storyTitle, "name"),
                        Pair(binding.storyDescription, "description")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        story?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoriesResponse.StoryDetails>() {
            override fun areItemsTheSame(oldItem: StoriesResponse.StoryDetails, newItem: StoriesResponse.StoryDetails): Boolean {
                return oldItem.storyId == newItem.storyId
            }

            override fun areContentsTheSame(oldItem: StoriesResponse.StoryDetails, newItem: StoriesResponse.StoryDetails): Boolean {
                return oldItem == newItem
            }
        }
    }
}

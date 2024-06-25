package com.example.storyshare.view.main

import LoginViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyshare.data.adapter.LoadingStateAdapter
import com.example.storyshare.data.adapter.StoryPagingAdapter
import com.example.storyshare.databinding.ActivityMainBinding
import com.example.storyshare.utils.ViewModelFactory
import com.example.storyshare.view.Maps.MapsActivity
import com.example.storyshare.view.Story.CreateStoryActivity
import com.example.storyshare.view.Story.StoryViewModel
import com.example.storyshare.view.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var storyPagingAdapter: StoryPagingAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.setHasFixedSize(true)

        setupViewModel()

        storyPagingAdapter = StoryPagingAdapter()

        storyViewModel.fetchCurrentUser().observe(this) {
            if (it.isLogin) {
                fetchStories()
                binding.tvLogo.setText("Hello, ${it.name}")
                showLoading(false)
            } else {
                navigateToLogin()
            }
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, CreateStoryActivity::class.java))
        }

        binding.ivmaps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.ivLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.ivLogout.setOnClickListener {
            logout()
        }
    }

    private fun setupViewModel() {
        viewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        storyViewModel = ViewModelProvider(this, viewModelFactory)[StoryViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun fetchStories() {
        binding.rvStories.adapter = storyPagingAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { storyPagingAdapter.retry() }
        )
        storyViewModel.fetchStories().observe(this) { storyList ->
            storyPagingAdapter.submitData(lifecycle, storyList)
            showLoading(false)
        }
    }

    private fun logout() {
        loginViewModel.signOut()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


}

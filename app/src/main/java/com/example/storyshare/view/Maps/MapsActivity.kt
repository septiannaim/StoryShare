package com.example.storyshare.view.Maps

import MapsViewModel
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyshare.R
import com.example.storyshare.databinding.ActivityMapsBinding
import com.example.storyshare.response.StoriesResponse
import com.example.storyshare.utils.Outcome
import com.example.storyshare.utils.ViewModelFactory
import com.example.storyshare.view.Story.DetailStoryActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mapsViewModel: MapsViewModel
    private val boundsBuilder = LatLngBounds.builder()
    private var isFirstStoryDisplayed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "User Story Map"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        mapsViewModel = ViewModelProvider(this, factory)[MapsViewModel::class.java]
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        displayUserStories()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                checkLocationPermission()
            }
        }

    private fun displayUserStories() {
        mapsViewModel.getUser().observe(this) { user ->
            val token = "Bearer ${user.token}"
            mapsViewModel.getLocationStory(token).observe(this) { result ->
                when (result) {
                    is Outcome.InProgress-> {}
                    is Outcome.Success -> addMarkers(result.value.stories)
                    is Outcome.Failure -> {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun addMarkers(stories: List<StoriesResponse.StoryDetails>) {
        stories.forEach { story ->
            val latLng = story.latitude?.let { lat -> story.longitude?.let { lon -> LatLng(lat, lon) } }
            latLng?.let {
                val markerOptions = MarkerOptions()
                    .position(it)
                    .title("User location: ${story.authorName}")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .alpha(0.7f)
                    .snippet(story.storyDescription)

                val marker = googleMap.addMarker(markerOptions)
                boundsBuilder.include(it)

                marker?.tag = story
                googleMap.setOnInfoWindowClickListener { marker ->
                    val intent = Intent(this, DetailStoryActivity::class.java).apply {
                        putExtra(DetailStoryActivity.EXTRA_STORY_DETAIL, marker.tag as StoriesResponse.StoryDetails)
                    }
                    startActivity(intent)
                }

                if (!isFirstStoryDisplayed) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
                    isFirstStoryDisplayed = true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
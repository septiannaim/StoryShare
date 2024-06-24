package com.example.storyshare.view.SplashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.storyshare.R
import com.example.storyshare.view.welcome.WelcomeActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var logoImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        logoImageView = findViewById(R.id.logo)

        // Set a delay to transition from the splash screen to the login activity
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to LoginActivity
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }, 3000) // 3 seconds delay
    }
}

package com.example.storyshare.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyshare.databinding.ActivitySignupBinding
import com.example.storyshare.utils.Outcome
import com.example.storyshare.utils.ViewModelFactory
import com.example.storyshare.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        playAnimation()
        binding.signupButton.setOnClickListener {
            registerAccount()
        }

        binding.tvlogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerAccount() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        viewModel.registerUser(name, email, password).observe(this) {
            when (it) {
                is Outcome.Success -> {
                    showLoading(false)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is Outcome.InProgress -> showLoading(true)
                is Outcome.Failure -> {
                    Toast.makeText(this, "Registration Failed: ${it.message}", Toast.LENGTH_LONG).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        // Title
        val titleAnim = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val subTitleAnim = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)

        // Name
        val nameLabelAnim = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameInputAnim = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)

        // Email
        val emailLabelAnim = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailInputAnim = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)

        // Password
        val passwordLabelAnim = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordInputAnim = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)

        // Button
        val registerButtonAnim = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        val titleSet = AnimatorSet().apply {
            playTogether(titleAnim, subTitleAnim)
        }

        val nameSet = AnimatorSet().apply {
            playTogether(nameLabelAnim, nameInputAnim)
        }

        val emailSet = AnimatorSet().apply {
            playTogether(emailLabelAnim, emailInputAnim)
        }

        val passwordSet = AnimatorSet().apply {
            playTogether(passwordLabelAnim, passwordInputAnim)
        }

        val buttonSet = AnimatorSet().apply {
            playTogether(registerButtonAnim)
        }

        AnimatorSet().apply {
            playSequentially(nameSet, titleSet, emailSet, passwordSet, buttonSet)
            start()
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[SignupViewModel::class.java]
    }
}

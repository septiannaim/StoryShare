package com.example.storyshare.view.login

import LoginViewModel
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyshare.data.pref.UserModel
import com.example.storyshare.databinding.ActivityLoginBinding
import com.example.storyshare.utils.Outcome
import com.example.storyshare.utils.ViewModelFactory
import com.example.storyshare.view.main.MainActivity
import com.example.storyshare.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
        animateViews()

        binding.loginButton.setOnClickListener {
            handleLogin()
        }

        binding.regis.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }

    private fun animateViews() {
        val titleAnimator = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val subtitleAnimator = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailLabelAnimator = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailInputAnimator = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordLabelAnimator = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordInputAnimator = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val loginButtonAnimator = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val registerButtonAnimator = ObjectAnimator.ofFloat(binding.regis, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                AnimatorSet().apply { playTogether(titleAnimator, subtitleAnimator) },
                AnimatorSet().apply { playTogether(emailLabelAnimator, emailInputAnimator) },
                AnimatorSet().apply { playTogether(passwordLabelAnimator, passwordInputAnimator) },
                AnimatorSet().apply { playTogether(loginButtonAnimator, registerButtonAnimator) }
            )
            start()
        }
    }

    private fun handleLogin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        viewModel.authenticateUser(email, password).observe(this, Observer {
            when (it) {
                is Outcome.InProgress -> showLoading(true)
                is Outcome.Success -> {
                    showLoading(false)
                    val response = it.value
                    saveUserSession(UserModel(response.loginDetails.userName, response.loginDetails.authToken, true))
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is Outcome.Failure -> {
                    showLoading(false)
                    Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun saveUserSession(user: UserModel) {
        viewModel.storeUser(user)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }
}

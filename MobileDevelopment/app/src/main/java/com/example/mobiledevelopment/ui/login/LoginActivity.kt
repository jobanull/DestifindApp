package com.example.mobiledevelopment.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mobiledevelopment.data.pref.LoginResult
import com.example.mobiledevelopment.data.response.LoginResponse
import com.example.mobiledevelopment.data.retrofit.ApiConfig
import com.example.mobiledevelopment.databinding.ActivityLoginBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.example.mobiledevelopment.ui.custom.CustomButton
import com.example.mobiledevelopment.ui.custom.CustomEmailText
import com.example.mobiledevelopment.ui.custom.CustomPasswordText
import com.example.mobiledevelopment.ui.main.MainActivity
import com.example.mobiledevelopment.util.setupTextWatcher
import com.example.mobiledevelopment.util.setupView
import com.example.mobiledevelopment.util.showToast
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginEmailText: CustomEmailText
    private lateinit var loginPassText: CustomPasswordText
    private lateinit var loginButton: CustomButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()

        // Custom View
        loginEmailText = binding.emailEditText
        loginPassText = binding.passwordEditText
        loginButton = binding.btnLogin
        setMyButtonEnable()

        validateEmail()
        validatePassword()
        setupAction(this)
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(500)
        val subTitle = ObjectAnimator.ofFloat(binding.description, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val emailForm = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val pw = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val pwForm = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(title, subTitle, email, emailForm, pw, pwForm,button)
            start()
        }
    }

    private fun setMyButtonEnable() {
        val result = loginEmailText.text
        loginButton.isEnabled = result != null && result.toString().isNotEmpty()
    }
    private fun validateEmail() {
        loginEmailText.setupTextWatcher {
            setMyButtonEnable()
        }
    }

    private fun validatePassword() {
        loginPassText.setupTextWatcher {
            setMyButtonEnable()
        }
    }

    private fun setupAction(context : Context) {
        binding.btnLogin.setOnClickListener{
            val email = loginEmailText.text.toString()
            val pass = loginPassText.text.toString()


            lifecycleScope.launch{
                try {
                    showLoading(true)
                    val apiService = ApiConfig.getApiService("")
                    val successResponse = apiService.login(email, pass)
                    viewModel.saveSession(LoginResult(email, successResponse.token))
                    successResponse.message?.let { it1 -> showToast(context, it1) }

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }catch (e: HttpException){
                    showLoading(true)
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                    errorResponse.message?.let { it1 -> showToast(context, it1) }
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
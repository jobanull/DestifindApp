package com.example.mobiledevelopment.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mobiledevelopment.data.response.LoginResponse
import com.example.mobiledevelopment.data.response.LoginResult
import com.example.mobiledevelopment.data.retrofit.ApiConfig
import com.example.mobiledevelopment.databinding.ActivityLoginBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.example.mobiledevelopment.ui.custom.CustomButton
import com.example.mobiledevelopment.ui.custom.CustomEmailText
import com.example.mobiledevelopment.ui.custom.CustomPasswordText
import com.example.mobiledevelopment.ui.main.MainActivity
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
        setupAction()
    }

    private fun setupView(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
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

    private fun validateEmail(){
        loginEmailText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
                setMyButtonEnable()
            }
        })
    }

    private fun validatePassword(){
        loginPassText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener{
            val email = loginEmailText.text.toString()
            val pass = loginPassText.text.toString()


            lifecycleScope.launch{
                try {
                    showLoading(true)
                    val apiService = ApiConfig.getApiService("")
                    val successResponse = apiService.login(email, pass)
                    viewModel.saveSession(LoginResult(email, successResponse.token))
                    successResponse.message?.let { it1 -> showToast(it1) }
                    showLoading(false)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("token", successResponse.token)
                    startActivity(intent)

                }catch (e: HttpException){
                    showLoading(true)
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                    errorResponse.message?.let { it1 -> showToast(it1) }
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
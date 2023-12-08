package com.example.mobiledevelopment.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import retrofit2.HttpException
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.mobiledevelopment.R
import com.example.mobiledevelopment.data.response.RegisterResponse
import com.example.mobiledevelopment.data.retrofit.ApiConfig
import com.example.mobiledevelopment.databinding.ActivityRegisterBinding
import com.example.mobiledevelopment.ui.custom.CustomButton
import com.example.mobiledevelopment.ui.custom.CustomEmailText
import com.example.mobiledevelopment.ui.custom.CustomPasswordText
import com.example.mobiledevelopment.ui.custom.CustomUsernameText
import com.google.gson.Gson
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var registerNameTxt: CustomUsernameText
    private lateinit var registerEmailText: CustomEmailText
    private lateinit var registerPasswordText: CustomPasswordText
    private lateinit var registerButton: CustomButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        playAnimation()

        // Custom View
        registerNameTxt = binding.usernameEditText
        registerEmailText = binding.emailEditText
        registerPasswordText = binding.passwordEditText
        registerButton = binding.btnSignUp
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
        val name = ObjectAnimator.ofFloat(binding.usernameEditText, View.ALPHA, 1f).setDuration(500)
        val nameForm = ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val emailForm = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val pw = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val pwForm = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(name, nameForm, email, emailForm, pw, pwForm, button)
            start()
        }
    }

    private fun validateEmail(){
        registerEmailText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }



    private fun validatePassword(){
        registerPasswordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setMyButtonEnable() {
        val result = registerEmailText.text
        registerButton.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun setupAction() {
        binding.btnSignUp.setOnClickListener {
            val name = registerNameTxt.text.toString()
            val email = registerEmailText.text.toString()
            val pass = registerPasswordText.text.toString()

            lifecycleScope.launch{
                try {
                    showLoading(true)
                    val apiService = ApiConfig.getApiService("")
                    val successResponse = apiService.register(name, email, pass)
                    successResponse.message?.let { it1 -> showToast(it1) }
                    showLoading(false)
                }catch (e: HttpException){
                    showLoading(true)
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    showToast(errorResponse.message)
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
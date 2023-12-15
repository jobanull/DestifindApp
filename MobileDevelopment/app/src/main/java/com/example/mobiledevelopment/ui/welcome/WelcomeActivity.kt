package com.example.mobiledevelopment.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mobiledevelopment.databinding.ActivityWelcomeBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.example.mobiledevelopment.ui.login.LoginActivity
import com.example.mobiledevelopment.ui.main.MainActivity
import com.example.mobiledevelopment.ui.register.RegisterActivity
import com.example.mobiledevelopment.util.setupView

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWelcomeBinding

    private val welcomeViewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(3000)
        installSplashScreen()

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        welcomeViewModel.getSession().observe(this){user->
            if(user.isLogin){
                showLoading(true)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        setupView()
        setupAction()
        playAnimation()
    }


    private fun setupAction(){
        binding.signIn.setOnClickListener{
            var toLogin = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(toLogin)
        }

        binding.signUp.setOnClickListener{
            var toLogin = Intent(this@WelcomeActivity, RegisterActivity::class.java)
            startActivity(toLogin)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val title = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.description, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.signIn, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signUp, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
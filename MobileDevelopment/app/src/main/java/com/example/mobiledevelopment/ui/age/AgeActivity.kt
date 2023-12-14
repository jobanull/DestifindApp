package com.example.mobiledevelopment.ui.age

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mobiledevelopment.R
import com.example.mobiledevelopment.data.response.LoginResult
import com.example.mobiledevelopment.databinding.ActivityAgeBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.example.mobiledevelopment.ui.main.MainActivity
import kotlinx.coroutines.launch

class AgeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgeBinding

    private val ageViewModel by viewModels<AgeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var name : String? = ""
    private var token : String? = ""
    private var category : String = ""
    private var selectedAge : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner: Spinner = binding.spinnerAge
        val ages = resources.getStringArray(R.array.age_array)
        val button: Button = binding.buttonAge

        ageViewModel.getSession().observe(this){
            user ->
            name = user.name
            token = user.token
            category = user.category
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                selectedAge = ages[position]

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }

        button.setOnClickListener {
            lifecycleScope.launch {
                ageViewModel.saveSession(LoginResult(name, token, category, selectedAge))
            }
            val intent = Intent(this@AgeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupView()
        playAnimation()
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
        ObjectAnimator.ofFloat(binding.image, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}
package com.example.mobiledevelopment.ui.category

import android.animation.AnimatorSet
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
import androidx.lifecycle.viewModelScope
import com.example.mobiledevelopment.R
import com.example.mobiledevelopment.data.pref.UserPreference
import com.example.mobiledevelopment.data.pref.dataStore
import com.example.mobiledevelopment.data.response.LoginResult
import com.example.mobiledevelopment.databinding.ActivityCategoryBinding
import com.example.mobiledevelopment.databinding.ActivityMainBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.example.mobiledevelopment.ui.main.MainActivity
import com.example.mobiledevelopment.ui.main.MainViewModel
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding

    private val categoryViewModel by viewModels<CategoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var name : String? = ""
    private var token : String? = ""
    private var selectedCategory : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner: Spinner = binding.spinnerCategories
        val categories = resources.getStringArray(R.array.category_array)
        val button: Button = binding.buttonCategory

        categoryViewModel.getSession().observe(this){
                user ->
             name = user.name
             token = user.token


        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                 selectedCategory = categories[position]

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }

        button.setOnClickListener {
            lifecycleScope.launch {
                categoryViewModel.saveSession(LoginResult(name, token,selectedCategory))
            }
            val intent = Intent(this@CategoryActivity, MainActivity::class.java)
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



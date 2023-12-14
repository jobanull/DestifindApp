package com.example.mobiledevelopment.ui.category

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mobiledevelopment.R
import com.example.mobiledevelopment.data.pref.LoginResult
import com.example.mobiledevelopment.databinding.ActivityCategoryBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.example.mobiledevelopment.ui.main.MainActivity
import com.example.mobiledevelopment.util.setupView
import com.example.mobiledevelopment.util.showToast
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding

    private val categoryViewModel by viewModels<CategoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner: Spinner = binding.spinnerCategories
        val categories = resources.getStringArray(R.array.category_array)
        val button: Button = binding.buttonCategory

        categoryViewModel.getSession().observe(this){
                user ->
             email = user.email
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
                try {
                    showLoading(true)
                    categoryViewModel.saveSession(LoginResult(email, token,selectedCategory))
                    showLoading(false)

                    val intent = Intent(this@CategoryActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }catch (e : Exception){
                    showToast(this@CategoryActivity, "Unexpected error : ${e.message}")
                }
            }

        }


        setupView()
        playAnimation()
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.image, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    companion object{
        private var email : String? = ""
        private var token : String? = ""
        private var selectedCategory : String = ""
    }
}



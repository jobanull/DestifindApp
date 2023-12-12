package com.example.mobiledevelopment.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.mobiledevelopment.R
import com.example.mobiledevelopment.data.response.ListDestinationItem
import com.example.mobiledevelopment.databinding.ActivityDetailBinding
import com.example.mobiledevelopment.ui.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailStory = intent.getParcelableExtra<ListDestinationItem>(KEY) as ListDestinationItem

        setupUi(detailStory)
    }

    private fun setupUi(data : ListDestinationItem){
        showLoading(true)
        Glide.with(this@DetailActivity)
            .load(data.photoUrl)
            .fitCenter()
            .into(binding.image)

        data.apply {
            binding.title.text = name
            binding.description.text = description
        }
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



    companion object{
        const val KEY = "key_story"
    }
}
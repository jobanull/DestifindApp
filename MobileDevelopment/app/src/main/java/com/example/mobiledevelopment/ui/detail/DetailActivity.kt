package com.example.mobiledevelopment.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.mobiledevelopment.R
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

        val intent = this.intent.getStringExtra(KEY)

        detailViewModel.user.observe(this){
            it?.let { binding.title.text = it.story?.name }
            it?.let { binding.description.text = it.story?.description }
            Glide.with(baseContext).load(it.story?.photoUrl).into(binding.image)
        }

        detailViewModel.getSession().observe(this) { user ->
            user.token?.let { detailViewModel.findDetail(it, intent.toString()) }
        }
    }



    companion object{
        const val KEY = "key_story"
    }
}
package com.example.mobiledevelopment.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.response.DetailResponse
import com.example.mobiledevelopment.data.response.LoginResult
import com.example.mobiledevelopment.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    init {
        getSession()
    }

    companion object {
        const val TAG = "DetailViewModel"
    }
}
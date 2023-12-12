package com.example.mobiledevelopment.ui.main

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.pref.UserModel
import com.example.mobiledevelopment.data.response.DestinationResponse
import com.example.mobiledevelopment.data.response.ListDestinationItem
import com.example.mobiledevelopment.data.response.LoginResult
import com.example.mobiledevelopment.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listDst = MutableLiveData<List<ListDestinationItem>>()
    val listDst :LiveData<List<ListDestinationItem>> = _listDst

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    var currentLatitude: Double = 0.0
    var currentLongitude: Double = 0.0

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    init {
        getSession()
    }

    fun getStories(token: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Make the API call using the updated getStories function in UserRepository
                val response = repository.getStories("Bearer $token", latitude, longitude)

                if (response.isSuccessful) {
                    // Handle a successful response
                    _listDst.value = response.body()?.listStory
                } else {
                    // Handle an unsuccessful response
                    // You might want to show an error message to the user
                }
            } catch (e: Exception) {
                // Handle exceptions
                // You might want to show an error message to the user
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        currentLatitude = latitude
        currentLongitude = longitude
    }
}
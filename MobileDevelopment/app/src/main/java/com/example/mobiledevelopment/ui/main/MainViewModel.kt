package com.example.mobiledevelopment.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.pref.LoginResult
import com.example.mobiledevelopment.data.response.ListDestinationItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listDst = MutableLiveData<List<ListDestinationItem>>()
    val listDst :LiveData<List<ListDestinationItem>> = _listDst

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currentLatitude = MutableLiveData<Double>()
    val currentLatitude : LiveData<Double> = _currentLatitude

    private val _currentLongitude = MutableLiveData<Double>()
    val currentLongitude : LiveData<Double> = _currentLongitude


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

    fun getStories(token: String, latitude: Double, longitude: Double,  age: Int,category: String,) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getStories("Bearer $token", latitude, longitude, age,category)

                if (response.isSuccessful) {
                    _listDst.value = response.body()?.listDst
                } else {
                }
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        _currentLatitude.value = latitude
        _currentLongitude.value = longitude
    }
}
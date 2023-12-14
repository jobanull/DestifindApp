package com.example.mobiledevelopment.ui.main

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
                val response = repository.getStories("Bearer $token", latitude, longitude)

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
        currentLatitude = latitude
        currentLongitude = longitude
    }
}
package com.example.mobiledevelopment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.pref.UserModel
import com.example.mobiledevelopment.data.response.ListStoryItem
import com.example.mobiledevelopment.data.response.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listUsers = MutableLiveData<List<ListStoryItem>>()
    val listUsers :LiveData<List<ListStoryItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
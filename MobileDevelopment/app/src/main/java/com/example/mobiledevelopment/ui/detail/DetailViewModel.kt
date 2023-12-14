package com.example.mobiledevelopment.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.pref.LoginResult

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    init {
        getSession()
    }
}
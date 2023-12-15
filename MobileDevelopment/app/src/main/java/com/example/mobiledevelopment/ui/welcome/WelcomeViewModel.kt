package com.example.mobiledevelopment.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.pref.LoginResult

class WelcomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    init {
        getSession()
    }
}
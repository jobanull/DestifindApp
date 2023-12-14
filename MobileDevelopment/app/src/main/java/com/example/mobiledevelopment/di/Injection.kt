package com.example.mobiledevelopment.di

import android.content.Context
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.pref.UserPreference
import com.example.mobiledevelopment.data.pref.dataStore
import com.example.mobiledevelopment.data.retrofit.ApiConfig
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getToken() }
        val apiService = ApiConfig.getApiService(user.toString())
        return UserRepository.getInstance(apiService, pref)
    }
}
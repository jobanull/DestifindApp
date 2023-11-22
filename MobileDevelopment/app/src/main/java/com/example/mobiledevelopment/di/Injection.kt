package com.example.mobiledevelopment.di

import android.content.Context
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.pref.UserPreference
import com.example.mobiledevelopment.data.pref.dataStore
import com.example.mobiledevelopment.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}
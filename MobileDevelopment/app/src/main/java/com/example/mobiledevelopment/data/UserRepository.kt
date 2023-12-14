package com.example.mobiledevelopment.data

import com.example.mobiledevelopment.data.pref.LoginResult
import com.example.mobiledevelopment.data.pref.UserPreference
import com.example.mobiledevelopment.data.response.DestinationResponse
import com.example.mobiledevelopment.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {


    suspend fun saveSession(user: LoginResult) {
        userPreference.saveSession(user)
    }
    suspend fun getStories(auth: String, lat: Double, lon: Double, age: Int, category : String): Response<DestinationResponse> {
        return apiService.getStories(auth, lat, lon, age, category)
//        return apiService.getStories(auth, -6.208560190493255, 106.77456829617012)
    }
    fun getSession(): Flow<LoginResult> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService,userPreference)
            }.also { instance = it }
    }
}
package com.example.mobiledevelopment.data

import com.example.mobiledevelopment.data.pref.LoginResult
import com.example.mobiledevelopment.data.pref.UserPreference
import com.example.mobiledevelopment.data.response.DestinationResponse
import com.example.mobiledevelopment.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response


class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {


    suspend fun saveSession(user: LoginResult) {
        userPreference.saveSession(user)
    }
    suspend fun getStories(auth: String, lat: Double, long: Double, age: Int, category : String): Response<DestinationResponse> {
        val jsonObject = JSONObject()
        jsonObject.put("lat", lat)
        jsonObject.put("long", long)
        jsonObject.put("age", age)
        jsonObject.put("category", category)

        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        return apiService.getStories(auth,  requestBody)
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
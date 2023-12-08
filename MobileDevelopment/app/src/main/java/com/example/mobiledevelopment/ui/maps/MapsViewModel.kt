package com.example.mobiledevelopment.ui.maps

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.response.DestinationResponse
import com.example.mobiledevelopment.data.response.LoginResult
import com.example.mobiledevelopment.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel (private val repository: UserRepository) : ViewModel() {

    private val _listUsers = MutableLiveData<DestinationResponse>()
    val listUsers : LiveData<DestinationResponse> = _listUsers

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    init {
        getSession()
    }

    fun findUsers(token : String)  {
        val client = ApiConfig.getApiService(token).getStoriesWithLocation()
        client.enqueue(object : Callback<DestinationResponse> {
            override fun onResponse(call: Call<DestinationResponse>, response: Response<DestinationResponse>) {
                if(response.isSuccessful){
                    _listUsers.value = response.body()
                }else{
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DestinationResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure : ${t.message.toString()}")
            }
        })
    }
}
package com.example.mobiledevelopment.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mobiledevelopment.data.UserRepository
import com.example.mobiledevelopment.data.response.DetailResponse
import com.example.mobiledevelopment.data.response.LoginResult
import com.example.mobiledevelopment.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _users = MutableLiveData<DetailResponse>()
    val user: LiveData<DetailResponse> = _users

    private val _isLoading = MutableLiveData<Boolean>()


    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    init {
        getSession()
    }


    fun findDetail(token : String, id: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getDetailStory(id)
        client
            .enqueue(object : Callback<DetailResponse> {
                override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                    _isLoading.value = false
                    if(response.isSuccessful){
                        _users.value = response.body()

                    }else{
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure : ${t.message.toString()}")
                }
            })
    }

    companion object {
        const val TAG = "DetailViewModel"
    }
}
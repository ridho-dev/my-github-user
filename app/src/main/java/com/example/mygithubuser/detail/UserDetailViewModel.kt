package com.example.mygithubuser.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuser.api.ApiConfig
import com.example.mygithubuser.api.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel: ViewModel() {
    private val _users = MutableLiveData<UserDetailResponse>()
    val users : LiveData<UserDetailResponse> = _users

    fun getUserDetail(userName: String): LiveData<UserDetailResponse> {
        val client = ApiConfig.getApiService().getDetailUser(userName)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _users.value = responseBody
                    }
                } else {
                    Log.e(UserDetailActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.e(UserDetailActivity.TAG, "onFailure: ${t.message}")
            }
        })
        return _users
    }
}
package com.example.mygithubuser.api

import com.example.mygithubuser.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("detail/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<UserResponse>
}
package com.example.mygithubuser.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowersUser(
        @Path("username") username: String
    ): Call<List<FollowersResponseItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<List<FollowingResponseItem>>
}
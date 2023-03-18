package com.isfan17.githubusers.data.remote.api

import com.isfan17.githubusers.BuildConfig
import com.isfan17.githubusers.data.remote.response.SearchUserResponse
import com.isfan17.githubusers.data.model.User
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("users")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun getUsers(): List<User>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): User

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun getUserFollowers(
        @Path("username") username: String
    ): List<User>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun getUserFollowing(
        @Path("username") username: String
    ): List<User>

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun getSearchUsers(
        @Query("q") query: String
    ): SearchUserResponse
}
package com.example.githubferdi.api

import com.example.githubferdi.UserResponse.DetailRes
import com.example.githubferdi.UserResponse.User
import com.example.githubferdi.UserResponse.UserRes
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("search/users")
    fun getSearchUser(
        @Query("q") query: String
    ): Call<UserRes>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailRes>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    companion object {

        private val client = OkHttpClient()

        fun createRequest(url: String, apiKey: String): Request {
            return Request.Builder()
                .url(url)
                .addHeader("Authorization", "token $apiKey")
                .build()
        }
    }
}
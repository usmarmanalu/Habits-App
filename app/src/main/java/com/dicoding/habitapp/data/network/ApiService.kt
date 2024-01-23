package com.dicoding.habitapp.data.network

import com.dicoding.habitapp.data.response.*
import retrofit2.*
import retrofit2.http.*

interface ApiService {

    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}
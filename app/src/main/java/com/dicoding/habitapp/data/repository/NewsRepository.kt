package com.dicoding.habitapp.data.repository

import android.util.*
import com.dicoding.habitapp.*
import com.dicoding.habitapp.data.network.*
import com.dicoding.habitapp.data.response.*
import retrofit2.*

class NewsRepository(private val apiService: ApiService) {

    interface NewsCallback {
        fun onSuccess(newsResponse: NewsResponse)
        fun onError(message: String)
    }

    fun getNews(country: String, callback: NewsCallback) {
        val call = apiService.getTopHeadlines(country, BuildConfig.APIKEY)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse != null) {
                        callback.onSuccess(newsResponse)
                    } else {
                        Log.d(TAG, "Response body is null")
                    }
                    Log.d(TAG, "Network request successful. Parsing response...")
                } else {
                    Log.d(TAG, "Response not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d(TAG, "Network request failed. ${t.message}")
            }
        })
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(apiService: ApiService): NewsRepository {
            return instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService)
            }.also { instance = it }
        }

        const val TAG = "NewsRepository"
    }
}

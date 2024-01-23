package com.dicoding.habitapp.di

import android.content.*
import com.dicoding.habitapp.data.local.*
import com.dicoding.habitapp.data.network.*
import com.dicoding.habitapp.data.repository.*

object Injection {
    fun provideRepository(context: Context): HabitRepository {
        val database = HabitDatabase.getInstance(context)
        val dao = database.habitDao()
        return HabitRepository.getInstance(dao)
    }

    fun provideNewsRepository(): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }
}
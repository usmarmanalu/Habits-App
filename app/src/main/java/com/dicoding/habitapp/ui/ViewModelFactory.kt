package com.dicoding.habitapp.ui

import android.content.*
import androidx.lifecycle.*
import com.dicoding.habitapp.data.*
import com.dicoding.habitapp.data.repository.*
import com.dicoding.habitapp.di.*
import com.dicoding.habitapp.ui.add.*
import com.dicoding.habitapp.ui.detail.*
import com.dicoding.habitapp.ui.list.*
import com.dicoding.habitapp.ui.news.*
import com.dicoding.habitapp.ui.random.*

class ViewModelFactory private constructor(
    private val habitRepository: HabitRepository,
    private val newsRepository: NewsRepository,
) :
    ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideNewsRepository()
                )
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HabitListViewModel::class.java) -> {
                HabitListViewModel(habitRepository) as T
            }

            modelClass.isAssignableFrom(AddHabitViewModel::class.java) -> {
                AddHabitViewModel(habitRepository) as T
            }

            modelClass.isAssignableFrom(DetailHabitViewModel::class.java) -> {
                DetailHabitViewModel(habitRepository) as T
            }

            modelClass.isAssignableFrom(RandomHabitViewModel::class.java) -> {
                RandomHabitViewModel(habitRepository) as T
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel(newsRepository) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}
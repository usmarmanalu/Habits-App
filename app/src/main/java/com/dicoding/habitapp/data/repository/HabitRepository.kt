package com.dicoding.habitapp.data.repository

import android.util.*
import androidx.lifecycle.*
import androidx.paging.*
import androidx.sqlite.db.*
import com.dicoding.habitapp.data.*
import com.dicoding.habitapp.data.local.*
import com.dicoding.habitapp.utils.*

class HabitRepository(
    private val habitDao: HabitDao,
) {
    companion object {

        @Volatile
        private var instance: HabitRepository? = null
        const val PAGE_SIZE = 20
        const val ENABLE_PLACEHOLDERS = true

        fun getInstance(habitDao: HabitDao): HabitRepository = instance ?: synchronized(this) {
            HabitRepository(habitDao).apply {
                instance = this
            }
        }

        const val TAG = "HabitRepository"
    }

    // Use SortUtils.getSortedQuery to create sortable query and build paged list
    fun getHabits(sortType: HabitSortType): LiveData<PagedList<Habit>> {
        val query = SortUtils.getSortedQuery(sortType)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
            .setPageSize(PAGE_SIZE)
            .build()
        Log.d(TAG, "Getting habits with sort type: $sortType, query: $query")
        return LivePagedListBuilder(habitDao.getHabits(query), config).build()
    }

    // Complete other function inside repository
    fun getHabitById(habitId: Int): LiveData<Habit> {
        Log.d(TAG, "Getting habit by ID: $habitId")
        return habitDao.getHabitById(habitId)
    }

    fun insertHabit(newHabit: Habit): Long {
        Log.d(TAG, "Inserting new habit: $newHabit")
        return habitDao.insertHabit(newHabit)
    }

    fun deleteHabit(habit: Habit) {
        Log.d(TAG, "Deleting habit: $habit")
        habitDao.deleteHabit(habit)
    }

    fun getRandomHabitByPriorityLevel(level: String): LiveData<Habit> {
        Log.d(TAG, "Getting random habit by priority level: $level")
        return habitDao.getRandomHabitByPriorityLevel(level)
    }

    fun searchHabits(query: String): LiveData<PagedList<Habit>> {
        val searchQuery = SimpleSQLiteQuery("SELECT * FROM habits WHERE title LIKE '%$query%'")
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
            .setPageSize(PAGE_SIZE)
            .build()
        Log.d(TAG, "Searching habits with query: $query")
        return LivePagedListBuilder(habitDao.searchHabits(searchQuery), config).build()
    }

     fun getHabitByStartTimeAndDuration(startTime: String, duration: Long): Habit? {
        return habitDao.getHabitByStartTimeAndDuration(startTime, duration)
    }
}
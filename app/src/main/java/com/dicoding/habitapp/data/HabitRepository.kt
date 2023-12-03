package com.dicoding.habitapp.data

import android.content.*
import androidx.lifecycle.*
import androidx.paging.*
import com.dicoding.habitapp.utils.*
import java.util.concurrent.*

class HabitRepository(
    private val habitDao: HabitDao,
    private val executor: ExecutorService
) {

    companion object {

        @Volatile
        private var instance: HabitRepository? = null
        const val PAGE_SIZE = 20
        const val ENABLE_PLACEHOLDERS = true

        fun getInstance(context: Context): HabitRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = HabitDatabase.getInstance(context)
                    instance = HabitRepository(
                        database.habitDao(),
                        Executors.newSingleThreadExecutor()
                    )
                }
                return instance as HabitRepository
            }
        }
    }


    //TODO 4 : Use SortUtils.getSortedQuery to create sortable query and build paged list
    fun getHabits(sortType: HabitSortType): LiveData<PagedList<Habit>> {
        val query = SortUtils.getSortedQuery(sortType)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
            .setPageSize(PAGE_SIZE)
            .build()
        return LivePagedListBuilder(habitDao.getHabits(query), config).build()
    }

    //TODO 5 : Complete other function inside repository
    fun getHabitById(habitId: Int): LiveData<Habit> = habitDao.getHabitById(habitId)

    fun insertHabit(newHabit: Habit): Long {
        return habitDao.insertHabit(newHabit)
    }

    fun deleteHabit(habit: Habit) {
        executor.execute {
            habitDao.deleteHabit(habit)
        }
    }

    fun getRandomHabitByPriorityLevel(level: String): LiveData<Habit> =
        habitDao.getRandomHabitByPriorityLevel(level)
}
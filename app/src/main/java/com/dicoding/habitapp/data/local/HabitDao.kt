package com.dicoding.habitapp.data.local

import androidx.lifecycle.*
import androidx.paging.*
import androidx.room.*
import androidx.sqlite.db.*
import com.dicoding.habitapp.data.*
import com.dicoding.habitapp.utils.*

// Define data access object (DAO)
@Dao
interface HabitDao {

    // Fungsi untuk mendapatkan kebiasaan (habits) berdasarkan query SQL raw
    @RawQuery(observedEntities = [Habit::class])
    fun getHabits(query: SupportSQLiteQuery): DataSource.Factory<Int, Habit>

    // Fungsi untuk mendapatkan kebiasaan (habit) berdasarkan ID dengan LiveData
    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitById(habitId: Int): LiveData<Habit>

    // Fungsi untuk menyisipkan atau mengganti habit ke dalam database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabit(habit: Habit): Long

    // Fungsi untuk menyisipkan atau mengganti beberapa habits ke dalam database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg habits: Habit)

    // Fungsi untuk mencari habits berdasarkan query SQL raw
    @RawQuery(observedEntities = [Habit::class])
    fun searchHabits(query: SupportSQLiteQuery): DataSource.Factory<Int, Habit>

    // Fungsi untuk menghapus habit dari database
    @Delete
    fun deleteHabit(habits: Habit)

    // Fungsi untuk mendapatkan habit acak berdasarkan tingkat prioritas dengan LiveData
    @Query("SELECT * FROM habits WHERE priorityLevel =:level ORDER BY RANDOM() LIMIT 1")
    fun getRandomHabitByPriorityLevel(level: String): LiveData<Habit>

    @Query("SELECT * FROM habits WHERE startTime = :startTime AND minutesFocus = :duration LIMIT 1")
    fun getHabitByStartTimeAndDuration(startTime: String, duration: Long): Habit?

}

package com.dicoding.habitapp.ui.add

import androidx.lifecycle.ViewModel
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.data.repository.HabitRepository

class AddHabitViewModel(private val habitRepository: HabitRepository) : ViewModel() {
    fun saveHabit(habit: Habit) {
        habitRepository.insertHabit(habit)
    }

     fun isDuplicateHabit(startTime: String, duration: Long): Boolean {
        val existingHabit = habitRepository.getHabitByStartTimeAndDuration(startTime, duration)
        return existingHabit != null
    }
}
package com.dicoding.habitapp.ui.list

import androidx.lifecycle.*
import androidx.paging.*
import com.dicoding.habitapp.*
import com.dicoding.habitapp.data.*
import com.dicoding.habitapp.data.repository.*
import com.dicoding.habitapp.utils.*
import kotlinx.coroutines.*

class HabitListViewModel(private val habitRepository: HabitRepository) : ViewModel() {

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _sort = MutableLiveData<HabitSortType>()

    val habits: LiveData<PagedList<Habit>> = _sort.switchMap {
        habitRepository.getHabits(it)
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _undo = MutableLiveData<Event<Habit>>()
    val undo: LiveData<Event<Habit>> = _undo

    init {
        _sort.value = HabitSortType.START_TIME
    }

    fun refreshHabits() {
        _isRefreshing.value = true
        viewModelScope.launch {
            _isRefreshing.postValue(false)
        }
    }


    fun sort(sortType: HabitSortType) {
        _sort.value = sortType
    }

    fun deleteHabit(habit: Habit) {
        habitRepository.deleteHabit(habit)
        _snackbarText.value = Event(R.string.habit_deleted)
        _undo.value = Event(habit)
    }

    fun insert(habit: Habit) {
        habitRepository.insertHabit(habit)
    }

    fun searchHabits(query: String): LiveData<PagedList<Habit>> =
        habitRepository.searchHabits(query)
}
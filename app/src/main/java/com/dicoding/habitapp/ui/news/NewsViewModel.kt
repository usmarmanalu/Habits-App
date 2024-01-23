package com.dicoding.habitapp.ui.news

import androidx.lifecycle.*
import com.dicoding.habitapp.data.repository.*
import com.dicoding.habitapp.data.response.*

class NewsViewModel(private val repository: NewsRepository): ViewModel() {

    private val _newsData = MutableLiveData<NewsResponse>()
    val newsData: LiveData<NewsResponse> get() = _newsData

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    fun getNews(country: String) {
        repository.getNews(country, object : NewsRepository.NewsCallback {
            override fun onSuccess(newsResponse: NewsResponse) {
                _newsData.postValue(newsResponse)
            }

            override fun onError(message: String) {
                _errorState.postValue(message)
            }
        })
    }
}


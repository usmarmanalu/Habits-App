package com.dicoding.habitapp.ui.news

import androidx.arch.core.executor.testing.*
import androidx.lifecycle.*
import com.dicoding.habitapp.data.repository.*
import com.dicoding.habitapp.data.response.*
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.*
import org.junit.runner.*
import org.powermock.core.classloader.annotations.*
import org.powermock.modules.junit4.*

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("DEPRECATION")
@RunWith(PowerMockRunner::class)
@PrepareForTest(NewsRepository::class)
class NewsViewModelTest {

    @get:Rule
    val instantExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(TestCoroutineDispatcher())

    private val newsRepository: NewsRepository = mock()
    private val observerNewsData: Observer<NewsResponse> = mock()
    private val observerErrorState: Observer<String> = mock()

    private val viewModel = NewsViewModel(newsRepository)

    private val dummyStories = DataDummy.generateDummyNewsResponse()

    @Test
    fun `getNews should update LiveData with news data`() {
        // Given
        val dummyCountry = "us"
        val captor = argumentCaptor<NewsRepository.NewsCallback>()

        // When
        viewModel.getNews(dummyCountry)

        // Then
        verify(newsRepository).getNews(eq(dummyCountry), captor.capture())

        // Simulate success callback from repository
        val dummyNewsResponse =
            NewsResponse(totalResults = dummyStories.size, articles = dummyStories, status = "ok")
        captor.firstValue.onSuccess(dummyNewsResponse)

        // Verify LiveData updates
        verify(observerNewsData).onChanged(dummyNewsResponse)
        verify(observerErrorState, never()).onChanged(any())
    }

    @Test
    fun `getNews should update LiveData with error state`() {
        // Given
        val dummyCountry = "us"
        val captor = argumentCaptor<NewsRepository.NewsCallback>()

        // When
        viewModel.getNews(dummyCountry)

        // Then
        verify(newsRepository).getNews(eq(dummyCountry), captor.capture())

        // Simulate error callback from repository
        val errorMessage = "Network error"
        captor.firstValue.onError(errorMessage)

        // Verify LiveData updates
        verify(observerErrorState).onChanged(errorMessage)
        verify(observerNewsData, never()).onChanged(any())
    }
}

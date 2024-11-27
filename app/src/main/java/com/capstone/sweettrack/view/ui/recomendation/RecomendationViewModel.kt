package com.capstone.sweettrack.view.ui.recomendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.sweettrack.data.response.ListEventsItem

class RecomendationViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _listEventsItem = MutableLiveData<List<ListEventsItem>>()
    val listEventsItem: LiveData<List<ListEventsItem>> get() = _listEventsItem

    init {
        fetchRecommendations()
    }

    private fun fetchRecommendations() {
        // Simulate loading data
        _isLoading.value = true

        // Fetch data from a repository or service
        // For demonstration, we will use a mock data
        val mockData = listOf<ListEventsItem>(
            ListEventsItem(id = 1, name = "Event 1"),
            ListEventsItem(id = 2, name = "Event 2"),
            // Add more mock events as needed
        )

        // Simulate a delay
        Thread {
            Thread.sleep(2000) // Simulate network delay
            _listEventsItem.postValue(mockData)
            _isLoading.postValue(false)
        }.start()
    }
}
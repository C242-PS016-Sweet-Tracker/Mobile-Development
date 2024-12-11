package com.capstone.sweettrack.view.ui.recomendation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.local.entity.FavoriteFood
import com.capstone.sweettrack.data.local.helper.InitialDataSource
import com.capstone.sweettrack.data.remote.response.ListEventsItem
import kotlinx.coroutines.launch

class RecommendationViewModel(private val repository: Repository) : ViewModel() {
    private val _recommendations = MutableLiveData<List<FavoriteFood>>()
    val recommendations: LiveData<List<FavoriteFood>> get() = _recommendations

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchRecommendations() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Simulate fetching data from repository
                val recommendations = InitialDataSource.getDummyFavoriteFoods()
                _recommendations.value = recommendations
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching recommendations: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


//    fun fetchRecommendations() {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val response = repository.getRecommendation()
//                if (!response.error) {
//                    // Only pass the necessary data to the view
//                    _recommendations.value = response.listEvents.map {
//                        ListEventsItem(name = it.name, mediaCover = it.mediaCover, category = it.category)
//                    }
//                } else {
//                    _errorMessage.value = response.message
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = e.message ?: "An error occurred"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
}

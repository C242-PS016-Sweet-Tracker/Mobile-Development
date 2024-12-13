package com.capstone.sweettrack.view.ui.recomendation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.Recommendation
import kotlinx.coroutines.launch

class RecommendationViewModel(private val repository: Repository) : ViewModel() {
    private val _recommendations = MutableLiveData<List<Recommendation>>()
    val recommendations: LiveData<List<Recommendation>> get() = _recommendations

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchRecommendations(type: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getRecommendationFood(type)
                if (!response.error) {
                    _recommendations.postValue(response.rekomendasi)
                } else {
                    _errorMessage.postValue(response.message)
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching recommendations: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}

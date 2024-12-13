package com.capstone.sweettrack.view.ui.favorite


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.Favorite
import com.capstone.sweettrack.data.remote.response.FavoriteResponses
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: Repository) : ViewModel() {

    private val _favoriteList = MutableLiveData<List<Favorite>>()
    val favoriteList: MutableLiveData<List<Favorite>> get() = _favoriteList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _addResult = MutableLiveData<FavoriteResponses>()
    val addResult: MutableLiveData<FavoriteResponses> get() = _addResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getFavorite() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getFavoriteUser()
                _favoriteList.value = response.data
                println(_favoriteList)
                if (!response.error) {
                    _favoriteList.value = response.data
                } else {
                    _errorMessage.value = response.message
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching favorites: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

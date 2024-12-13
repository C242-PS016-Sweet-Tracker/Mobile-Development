package com.capstone.sweettrack.view.ui.history


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.History
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: Repository) : ViewModel() {

    private val _historyList = MutableLiveData<List<History>>()
    val historyList: LiveData<List<History>> get() = _historyList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getHistoryUser() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getHistoryUser()
                _historyList.postValue(response.data)
            } catch (e: Exception) {
                _historyList.postValue(emptyList())
                _errorMessage.postValue(e.localizedMessage ?: "An unexpected error occurred.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}


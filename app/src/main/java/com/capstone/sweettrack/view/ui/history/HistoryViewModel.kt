package com.capstone.sweettrack.view.ui.history


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.local.entity.HistoryScan
import com.capstone.sweettrack.data.remote.response.History
import kotlinx.coroutines.launch

class HistoryViewModel(private  val repository: Repository) : ViewModel() {

    private val _historyList = MutableLiveData<List<History>>()
    val historyList: LiveData<List<History>> get() = _historyList


    fun getHistoryUser() {
        viewModelScope.launch {
            val response = repository.getHistoryUser()
            _historyList.postValue(response.data)
        }
    }
}

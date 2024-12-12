package com.capstone.sweettrack.view.ui.history


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.local.entity.HistoryScan
import kotlinx.coroutines.launch

class HistoryViewModel(private  val repository: Repository) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryScan>>()
    val historyList: LiveData<List<HistoryScan>> get() = _historyList

    init {
        viewModelScope.launch {
            _historyList.value = repository.getAllHistory()
            println(_historyList.value)
        }
    }
}

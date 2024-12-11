package com.capstone.sweettrack.view.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.local.entity.HistoryScan
import com.capstone.sweettrack.data.local.room.SweetTrackDatabase
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val database = SweetTrackDatabase.getInstance(application, viewModelScope)
    private val dao = database.eventDao()

    private val _historyList = MutableLiveData<List<HistoryScan>>()
    val historyList: LiveData<List<HistoryScan>> get() = _historyList

    init {
        viewModelScope.launch {
            _historyList.value = dao.getAllHistories(102)
            println(_historyList.value)
        }
    }
}

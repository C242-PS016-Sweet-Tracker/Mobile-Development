package com.capstone.sweettrack.view.ui.result.resultocr

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.OcrResponse
import com.capstone.sweettrack.data.remote.response.ResponseModel
import kotlinx.coroutines.launch

class ResultOcrViewModel(private val repository: Repository) : ViewModel() {


    fun addResultToHistory(uri: String, data: OcrResponse) {
        viewModelScope.launch {
            println("data : $data}")
            repository.addResultOcrToHistoryScan(uri, data)

        }
    }

}
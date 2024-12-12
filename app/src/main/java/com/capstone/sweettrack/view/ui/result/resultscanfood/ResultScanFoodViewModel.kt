package com.capstone.sweettrack.view.ui.result.resultscanfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.ResponseModel
import kotlinx.coroutines.launch

class ResultScanFoodViewModel(private val repository: Repository) : ViewModel() {


    fun addResultToHistory(uri: String, data: ResponseModel) {
        viewModelScope.launch {
            repository.addToHistoryScan(uri, data)

        }
    }

    fun updateUserCalorieDay(calorie: Double) {
        viewModelScope.launch {
            repository.updateUserCalorieDay(calorie)
        }
    }

}
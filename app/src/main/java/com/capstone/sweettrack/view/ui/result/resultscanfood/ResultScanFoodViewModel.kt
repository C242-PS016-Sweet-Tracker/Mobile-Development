package com.capstone.sweettrack.view.ui.result.resultscanfood

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.ApiResponse
import com.capstone.sweettrack.data.remote.response.EditCalorieResponse
import com.capstone.sweettrack.data.remote.response.HistoryScanFoodRequest
import kotlinx.coroutines.launch

class ResultScanFoodViewModel(private val repository: Repository) : ViewModel() {

    private val _addResult = MutableLiveData<ApiResponse>()
    val addResult: LiveData<ApiResponse> get() = _addResult

    private val _updateResult = MutableLiveData<EditCalorieResponse>()
    val updateResult: LiveData<EditCalorieResponse> get() = _updateResult


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun addResultToHistory(
        fotoUri: Uri,
        foodName: String,
        calorie: Double,
        sugar: Double,
        fat: Double,
        protein: Double,
        context: Context
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.addFoodToHistory(
                    fotoUri,
                    foodName,
                    calorie,
                    sugar,
                    fat,
                    protein,
                    context
                )
                _addResult.postValue(response)
            } catch (e: Exception) {
                _addResult.postValue(
                    ApiResponse(
                        statusCode = 500,
                        error = true,
                        message = "Error",
                        describe = "Gagal menambahkan ke History"
                    )
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun updateUserCalorieDay(
        calorie: Double
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.updateUserCalorieDay(calorie)
                _updateResult.postValue(response)
            } catch (e: Exception) {
                _updateResult.postValue(
                    EditCalorieResponse(
                        statusCode = 500,
                        error = true,
                        message = "Error",
                        describe = "jika anda memakan makanan ini maka melebihi kalori harian anda"
                    )
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
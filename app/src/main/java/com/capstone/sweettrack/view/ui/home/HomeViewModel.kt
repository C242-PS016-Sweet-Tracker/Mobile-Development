package com.capstone.sweettrack.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.local.entity.FavoriteFood
import com.capstone.sweettrack.data.local.helper.InitialDataSource
import com.capstone.sweettrack.data.pref.UserModel
import com.capstone.sweettrack.data.remote.response.CaloriesResponse
import com.capstone.sweettrack.data.remote.response.Recommendation
import com.capstone.sweettrack.data.remote.response.RecommendationRequest
import com.capstone.sweettrack.data.remote.response.RecommendationResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _dataResult = MutableLiveData<CaloriesResponse>()
    val dataResult: LiveData<CaloriesResponse> get() = _dataResult

    private val _recommendations = MutableLiveData<List<Recommendation>>()
    val recommendations: LiveData<List<Recommendation>> get() = _recommendations

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getData()
    }

    private fun getData() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getUserCalorie()
                _dataResult.postValue(response)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val parsedError = Gson().fromJson(errorBody, CaloriesResponse::class.java)
                val errorResponse = CaloriesResponse(
                    statusCode = 500,
                    error = true,
                    message = parsedError?.message ?: "Gagal Mendapatkan data"
                )
                _dataResult.postValue(errorResponse)
            } catch (e: Exception) {
                _dataResult.postValue(
                    CaloriesResponse(statusCode = 500, error = true, message = "Kesalahan jaringan")
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun fetchRecommendations(type: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getRecommendationFood(type)
                if (!response.error) {
                    // Ambil daftar rekomendasi dari respons
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



    fun refreshData() {
        getData()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}
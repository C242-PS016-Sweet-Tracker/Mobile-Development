package com.capstone.sweettrack.view.ui.userinformation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.ApiResponse
import com.capstone.sweettrack.data.remote.response.DetailUserResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserInformationViewModel(private val repository: Repository) : ViewModel() {

    private val _getDataResult = MutableLiveData<DetailUserResponse>()
    val getDataResult: LiveData<DetailUserResponse> get() = _getDataResult

    private val _addDetailUserResult = MutableLiveData<ApiResponse>()
    val addDetailUserResult: LiveData<ApiResponse> get() = _addDetailUserResult

    private val _editDetailUserResult = MutableLiveData<ApiResponse>()
    val editDetailUserResult: LiveData<ApiResponse> get() = _editDetailUserResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading


    init {
        getUserDetailInformation()
    }

    private fun getUserDetailInformation() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getDetailUserInformation()
                _getDataResult.postValue(response)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val parsedError = Gson().fromJson(errorBody, DetailUserResponse::class.java)
                val errorResponse = DetailUserResponse(
                    statusCode = 500,
                    error = true,
                    message = parsedError?.message ?: "Gagal Mendapatkan data"
                )
                _getDataResult.postValue(errorResponse)
            } catch (e: Exception) {
                _getDataResult.postValue(
                    DetailUserResponse(
                        statusCode = 500,
                        error = true,
                        message = "Kesalahan jaringan"
                    )
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun addUserDetailInformation(
        name: String,
        gender: String,
        age: Int?,
        height: Double?,
        weight: Double?,
        activityLevel: String,
        diabetesType: String,
        lastBloodSugar: Double?
    ) {

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val message = repository.addDetailUserInformation(
                    name,
                    gender,
                    age,
                    height,
                    weight,
                    activityLevel,
                    diabetesType,
                    lastBloodSugar
                )
                if (message.error != true) {
                    _addDetailUserResult.value = message
                } else {
                    _addDetailUserResult.value = message
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
                _addDetailUserResult.value = errorResponse
            } catch (e: Exception) {
                _addDetailUserResult.value = ApiResponse(
                    500,
                    true,
                    "Gagal",
                    "Kesalahan Jaringan"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun editUserDetail(
        name: String,
        gender: String,
        age: Int?,
        height: Double?,
        weight: Double?,
        activityLevel: String,
        diabetesType: String,
        lastBloodSugar: Double?
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val message = repository.editDetailUserInformation(
                    name,
                    gender,
                    age,
                    height,
                    weight,
                    activityLevel,
                    diabetesType,
                    lastBloodSugar
                )
                println(message)
                if (message.error != true) {
                    _editDetailUserResult.value = message
                } else {
                    _editDetailUserResult.value = message
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
                _editDetailUserResult.value = errorResponse
            } catch (e: Exception) {
                _editDetailUserResult.value = ApiResponse(
                    500,
                    true,
                    "Gagal",
                    "Kesalahan Jaringan"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

}

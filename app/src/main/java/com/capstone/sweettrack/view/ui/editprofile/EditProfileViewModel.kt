package com.capstone.sweettrack.view.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.ApiResponse
import com.capstone.sweettrack.data.remote.response.EditProfileRequest
import com.capstone.sweettrack.data.remote.response.UserProfileResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EditProfileViewModel(private val repository: Repository) : ViewModel() {

    private val _dataResult = MutableLiveData<UserProfileResponse>()
    val dataResult: LiveData<UserProfileResponse> get() = _dataResult

    private val _updateResult = MutableLiveData<ApiResponse>()
    val updateResult: LiveData<ApiResponse> get() = _updateResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    init {
        getProfileUser()
    }

    private fun getProfileUser() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getProfileUser()
                _dataResult.postValue(response)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val parsedError = Gson().fromJson(errorBody, UserProfileResponse::class.java)
                val errorResponse = UserProfileResponse(
                    statusCode = 500,
                    error = true,
                    message = parsedError?.message ?: "Gagal Mendapatkan data"
                )
                _dataResult.postValue(errorResponse)
            } catch (e: Exception) {
                _dataResult.postValue(
                    UserProfileResponse(statusCode = 500, error = true, message = "Kesalahan jaringan")
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun editProfileUser(request: EditProfileRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.editProfileUser(request)
                if (!response.error) {
                    _updateResult.postValue(
                        ApiResponse(
                            statusCode = response.statusCode,
                            error = response.error,
                            message = "Profil berhasil diperbarui."
                        )
                    )

                    getProfileUser()
                } else {
                    _updateResult.postValue(response)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val parsedError = Gson().fromJson(errorBody, ApiResponse::class.java)
                _updateResult.postValue(
                    ApiResponse(
                        statusCode = 500,
                        error = true,
                        message = parsedError?.message ?: "Gagal Mengubah Profil"
                    )
                )
            } catch (e: Exception) {
                _updateResult.postValue(
                    ApiResponse(
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

}
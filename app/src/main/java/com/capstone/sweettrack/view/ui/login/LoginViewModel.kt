package com.capstone.sweettrack.view.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.pref.UserModel
import com.capstone.sweettrack.data.remote.response.LoginRequest
import com.capstone.sweettrack.data.remote.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse>() //Any karena belum ada response
    val loginResult: LiveData<LoginResponse> get() = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(email, password)
                val response = repository.login(loginRequest)
                _loginResult.postValue(response)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val parsedError = Gson().fromJson(errorBody, LoginResponse::class.java)
                val errorResponse = LoginResponse(
                    error = true,
                    message = parsedError?.message ?: "Login gagal"
                )
                _loginResult.postValue(errorResponse)
            } catch (e: Exception) {
                _loginResult.postValue(LoginResponse(error = true, message = "Kesalahan jaringan"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }


    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}

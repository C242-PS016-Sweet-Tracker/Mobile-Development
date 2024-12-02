package com.capstone.sweettrack.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.pref.UserModel

class HomeViewModel(
    private val repository: Repository
) : ViewModel() {


    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}
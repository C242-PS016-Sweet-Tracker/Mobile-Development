package com.capstone.sweettrack.view.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.sweettrack.data.Repository

class EditProfileViewModel(private val repository: Repository) : ViewModel() {

    private val _profileData = MutableLiveData<Profile>()
    val profileData: LiveData<Profile> get() = _profileData

    fun updateProfile(profile: Profile) {
        _profileData.value = profile
    }
}

data class Profile(
    val fullName: String,
    val username: String,
    val gender: String,
    val dateOfBirth: String
)
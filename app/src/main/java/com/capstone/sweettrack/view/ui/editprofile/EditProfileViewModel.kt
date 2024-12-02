package com.capstone.sweettrack.view.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditProfileViewModel : ViewModel() {

    private val _profileData = MutableLiveData<Profile>()
    val profileData: LiveData<Profile> get() = _profileData

    init {

        _profileData.value = Profile(
            fullName = "John Doe",
            username = "johndoe",
            email = "johndoe@example.com",
            dateOfBirth = "1990-01-01"
        )
    }

    fun updateProfile(profile: Profile) {
        _profileData.value = profile
    }
}

data class Profile(
    val fullName: String,
    val username: String,
    val email: String,
    val dateOfBirth: String
)
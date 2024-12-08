package com.capstone.sweettrack.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.di.Injection
import com.capstone.sweettrack.view.ui.authentication.AuthenticationViewModel
import com.capstone.sweettrack.view.ui.editprofile.EditProfileViewModel
import com.capstone.sweettrack.view.ui.home.HomeViewModel
import com.capstone.sweettrack.view.ui.login.LoginViewModel
import com.capstone.sweettrack.view.ui.newpassword.NewPasswordViewModel
import com.capstone.sweettrack.view.ui.profile.ProfileViewModel
import com.capstone.sweettrack.view.ui.recomendation.RecomendationViewModel
import com.capstone.sweettrack.view.ui.resetpassword.ResetPasswordViewModel
import com.capstone.sweettrack.view.ui.signup.SignUpViewModel
import com.capstone.sweettrack.view.ui.userinformation.UserInformationViewModel

class ViewModelFactory(
    private val repository: Repository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> {
                AuthenticationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ResetPasswordViewModel::class.java) -> {
                ResetPasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NewPasswordViewModel::class.java) -> {
                NewPasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UserInformationViewModel::class.java) -> {
                UserInformationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RecomendationViewModel::class.java) -> {
                RecomendationViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val repository = Injection.provideRepository(context)
                    INSTANCE = ViewModelFactory(repository)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}


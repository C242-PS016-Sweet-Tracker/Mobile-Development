package com.capstone.sweettrack.view

import DetailViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.di.Injection
import com.capstone.sweettrack.view.ui.authentication.AuthenticationViewModel
import com.capstone.sweettrack.view.ui.calculatorcalori.CalculatorViewModel
import com.capstone.sweettrack.view.ui.editprofile.EditProfileViewModel
import com.capstone.sweettrack.view.ui.favorite.FavoriteViewModel
import com.capstone.sweettrack.view.ui.history.HistoryViewModel
import com.capstone.sweettrack.view.ui.home.HomeViewModel
import com.capstone.sweettrack.view.ui.login.LoginViewModel
import com.capstone.sweettrack.view.ui.newpassword.NewPasswordViewModel
import com.capstone.sweettrack.view.ui.profile.ProfileViewModel
import com.capstone.sweettrack.view.ui.recomendation.RecommendationViewModel
import com.capstone.sweettrack.view.ui.resetpassword.ResetPasswordViewModel
import com.capstone.sweettrack.view.ui.result.resultocr.ResultOcrViewModel
import com.capstone.sweettrack.view.ui.result.resultscanfood.ResultScanFoodViewModel
import com.capstone.sweettrack.view.ui.scanfood.ScanFoodViewModel
import com.capstone.sweettrack.view.ui.signup.SignUpViewModel
import com.capstone.sweettrack.view.ui.splash.SplashViewModel
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

            modelClass.isAssignableFrom(RecommendationViewModel::class.java) -> {
                RecommendationViewModel(repository) as T
            }

            modelClass.isAssignableFrom(CalculatorViewModel::class.java) -> {
                CalculatorViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ScanFoodViewModel::class.java) -> {
                ScanFoodViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ResultScanFoodViewModel::class.java) -> {
                ResultScanFoodViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ResultOcrViewModel::class.java) -> {
                ResultOcrViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
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


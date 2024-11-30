package com.capstone.sweettrack.view.ui.signup

import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    fun validateInputs(username: String, email: String, password: String, confirmPassword: String): Boolean {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return false
        }

        if (password != confirmPassword) {
            return false
        }

        return true
    }
}

package com.capstone.sweettrack.view.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    // Simulasi login (dapat diganti dengan API di masa depan)
    suspend fun login(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            // Logika login sederhana
            email == "user@example.com" && password == "password123"
        }
    }
}

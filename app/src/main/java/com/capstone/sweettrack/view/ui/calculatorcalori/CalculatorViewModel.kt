package com.capstone.sweettrack.view.ui.calculatorcalori

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    val age = MutableLiveData<Int>()
    val height = MutableLiveData<Double>()
    val weight = MutableLiveData<Double>()
    val gender = MutableLiveData<String>()


    private val _calorieResult = MutableLiveData<Int>()
    val calorieResult: LiveData<Int> get() = _calorieResult

    fun setCalorieResult(result: Int) {
        _calorieResult.value = result
    }

    private val _selectedActivityLevel = MutableLiveData<String>()
    val selectedActivityLevel: LiveData<String> get() = _selectedActivityLevel

    fun setSelectedActivityLevel(activityLevel: String) {
        _selectedActivityLevel.value = activityLevel
    }

    fun calculateCalories() {
        // Ambil nilai input
        val age = age.value ?: 0
        val height = height.value ?: 0.0
        val weight = weight.value ?: 0.0
        val gender = gender.value ?: ""
        val activityLevel = selectedActivityLevel.value ?: "Tidak aktif"

        // Hitung BMR berdasarkan gender
        val bmr = if (gender.equals("Laki-laki", ignoreCase = true)) {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }

        // Tentukan faktor aktivitas
        val activityFactor = when (activityLevel) {
            "Tidak aktif" -> 1.2
            "Ringan" -> 1.375
            "Sedang" -> 1.55
            "Aktif" -> 1.725
            "Sangat aktif" -> 1.9
            else -> 1.0
        }

        // Hitung kebutuhan kalori harian
        val calorieResult = (bmr * activityFactor).toInt()

        // Simpan hasil ke LiveData
        setCalorieResult(calorieResult)
    }

}

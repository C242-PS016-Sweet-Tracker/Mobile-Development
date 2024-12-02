package com.capstone.sweettrack.view.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SettingViewModel : ViewModel() {

    private val _reminderEat = MutableLiveData<Boolean>(false)
    val reminderEat: LiveData<Boolean> get() = _reminderEat

    private val _reminderHealth = MutableLiveData<Boolean>(false)
    val reminderHealth: LiveData<Boolean> get() = _reminderHealth

    fun setReminderEat(value: Boolean) {
        _reminderEat.value = value
    }

    fun setReminderHealth(value: Boolean) {
        _reminderHealth.value = value
    }
}
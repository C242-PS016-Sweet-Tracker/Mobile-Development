package com.capstone.sweettrack.view.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.local.entity.FavoriteFood
import com.capstone.sweettrack.data.local.room.SweetTrackDatabase
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

//    private val database = SweetTrackDatabase.getInstance(application, viewModelScope)
//    private val dao = database.eventDao()
//
//    private val _favoriteList = MutableLiveData<List<FavoriteFood>>()
//    val favoriteList: LiveData<List<FavoriteFood>> get() = _favoriteList
//
//
//    init {
//        viewModelScope.launch {
//            _favoriteList.value = dao.getAllFavorites(101)
//        }
//    }
}
package com.capstone.sweettrack.view.ui.scanfood

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.sweettrack.data.remote.response.UploadResponse

class ScanFoodViewModel : ViewModel() {

    private val _uploadResult = MutableLiveData<UploadResponse>()
    private var uploadResult: LiveData<UploadResponse> = _uploadResult

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> get() = _currentImageUri

    fun setResultData(imageUri: Uri?) {
        _currentImageUri.value = imageUri
    }
}
package com.capstone.sweettrack.view.ui.scanfood

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.sweettrack.data.remote.response.UploadResponse
import com.yalantis.ucrop.UCrop
import java.io.File

class ScanFoodViewModel : ViewModel() {

    private val _uploadResult = MutableLiveData<UploadResponse>()
    private var uploadResult: LiveData<UploadResponse> = _uploadResult

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> get() = _currentImageUri


    fun setResultData(imageUri: Uri?) {
        _currentImageUri.value = imageUri
    }
    private val _lastSuccessfulImageUri = MutableLiveData<Uri?>()
    val lastSuccessfulImageUri: LiveData<Uri?> get() = _lastSuccessfulImageUri

    fun setCurrentImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }

    private fun setLastSuccessfulImageUri(uri: Uri?) {
        _lastSuccessfulImageUri.value = uri
    }

    fun resetToLastSuccessfulImageUri() {
        if (_lastSuccessfulImageUri.value != null) {
            _currentImageUri.value = _lastSuccessfulImageUri.value
        } else {
            Log.w("ScanFoodViewModel", "No last successful URI to reset to")
        }
    }


    fun getCropDestinationUri(context: Context): Uri {
        val timestamp = System.currentTimeMillis()
        val uniqueFileName = "croppedImage_$timestamp.jpg"
        return Uri.fromFile(File(context.cacheDir, uniqueFileName))
    }


    fun handleCropResult(data: Intent?) {
        val resultUri = data?.let { UCrop.getOutput(it) }
        if (resultUri != null) {
            setCurrentImageUri(resultUri)
            setLastSuccessfulImageUri(resultUri)
            Log.d("ScanFoodViewModel", "Crop successful: $resultUri")
        } else {
            Log.e("ScanFoodViewModel", "Failed to crop image: URI is null")
            setCurrentImageUri(null)
        }
    }

}

package com.capstone.sweettrack.view.ui.scanfood

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.OcrResponse
import com.capstone.sweettrack.data.remote.response.ResponseModel
import com.capstone.sweettrack.data.remote.response.UploadResponse
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File

class ScanFoodViewModel (private val repository: Repository) : ViewModel() {

    private val _ocrScanResult = MutableLiveData<OcrResponse>()
    var ocrScanResult: LiveData<OcrResponse> = _ocrScanResult

    private val _scanFoodResult = MutableLiveData<ResponseModel>()
    var scanFoodResult: LiveData<ResponseModel> = _scanFoodResult

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> get() = _currentImageUri

    private val _lastSuccessfulImageUri = MutableLiveData<Uri?>()
    val lastSuccessfulImageUri: LiveData<Uri?> get() = _lastSuccessfulImageUri

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun scanOcrNutrition (
        fotoUri: Uri?,
        context: Context
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.scanNutritionOcr(fotoUri, context)
                _ocrScanResult.postValue(response)
            } catch (e: Exception) {
                _ocrScanResult.postValue(
                    OcrResponse(
                        statusCode = 500,
                        error = true,
                        message = "Kesalahan jaringan atau server",
                        data = null
                    )
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun scanFoodNutrition (
        fotoUri: Uri?,
        context: Context
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.scanFoodNutrition(fotoUri, context)
                _scanFoodResult.postValue(response)
            } catch (e: Exception) {
                _scanFoodResult.postValue(
                    ResponseModel(
                        statusCode = 500,
                        error = true,
                        message = "Kesalahan jaringan atau server",
                        data = null
                    )
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun setResultData(imageUri: Uri?) {
        _currentImageUri.value = imageUri
    }

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

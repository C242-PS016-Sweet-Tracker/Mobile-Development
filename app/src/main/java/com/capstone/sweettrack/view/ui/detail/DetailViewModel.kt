import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.FavoriteAdd
import kotlinx.coroutines.launch

class DetailViewModel(private var repository: Repository) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun removeFavorite(favoriteAdd: FavoriteAdd) {
        viewModelScope.launch {
            try {
                repository.removeFavorite(favoriteAdd.namaMakanan)
                _isFavorite.value = false
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error removing favorite: ${e.message}")
            }
        }
    }

    fun checkIfFavorite(foodName: String) {
        viewModelScope.launch {
            val count = repository.isFoodFavorite(foodName)
            _isFavorite.value = count > 0
        }
    }


    fun addFavorite(favoriteAdd: FavoriteAdd) {
        viewModelScope.launch {
            try {
                val response = repository.addFavoriteUser(favoriteAdd)
                if (!response.error) {
                    _isFavorite.value = true
                } else {
                    Log.e("DetailViewModel", "Failed to add favorite: ${response.message}")
                    _isFavorite.value = false
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error adding favorite: ${e.message}")
                _isFavorite.value = false
            }
        }
    }
}

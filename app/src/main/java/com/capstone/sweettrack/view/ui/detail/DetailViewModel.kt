import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {

    // Status favorite (default false)
    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    // Fungsi untuk toggle status favorite
    fun toggleFavorite() {
        _isFavorite.value = _isFavorite.value != true
    }
}

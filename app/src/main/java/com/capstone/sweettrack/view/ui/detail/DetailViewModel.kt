import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.FavoriteAdd
import kotlinx.coroutines.launch

class DetailViewModel(private var repository: Repository) : ViewModel() {

    // Status favorite (default false)
//    private val _isFavorite = MutableLiveData(false)
    private val _isFavorite = MutableLiveData<Boolean>()
//    val isFavorite: LiveData<Boolean> = _isFavorite
    val isFavorite : LiveData<Boolean> get() = _isFavorite

    // Fungsi untuk toggle status favorite
    fun toggleFavorite() {
        _isFavorite.value = _isFavorite.value != true
    }

    fun removeFavorite(favoriteAdd: FavoriteAdd) {
        viewModelScope.launch {
            try {
                repository.removeFavorite(favoriteAdd.namaMakanan) // Assuming you want to remove by food name
                _isFavorite.value = false // Update the favorite status
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error removing favorite: ${e.message}")
                // Handle error if needed
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
                // Coba untuk menambahkan favorite
                val response = repository.addFavoriteUser(favoriteAdd)
                // Jika berhasil, update status favorit
                if (response.error!=true) {
                    _isFavorite.value = true
                } else {
                    // Jika gagal, bisa menampilkan pesan error
                    Log.e("DetailViewModel", "Failed to add favorite: ${response.message}")
                    _isFavorite.value = false
                }
            } catch (e: Exception) {
                // Tangani kesalahan yang terjadi (misalnya kesalahan jaringan atau lainnya)
                Log.e("DetailViewModel", "Error adding favorite: ${e.message}")
                // Tampilkan pesan kesalahan kepada pengguna atau lakukan penanganan lainnya jika diperlukan
                _isFavorite.value = false
            }
        }
    }
}

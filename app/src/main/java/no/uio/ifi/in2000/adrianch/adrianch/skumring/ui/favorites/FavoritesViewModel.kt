package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.favorites

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ApplicationSkumring
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.InternetException
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo

data class FavoritesUiState(
    val places: List<PlaceInfo> = emptyList(),

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

private const val logTag = "FavoritesViewModel"

@SuppressLint("StaticFieldLeak")
class FavoritesViewModel(
    private val context: Context,
    private val placeRepository: PlaceRepository
) : ViewModel() {
    private val _favoritesUiState = MutableStateFlow(FavoritesUiState())
    val favoritesUiState: StateFlow<FavoritesUiState> = _favoritesUiState.asStateFlow()

    init {
        //loadList()
    }

    fun loadList() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoritesUiState.update { currentfavoritesUiState ->
                try {
                    val placeSummaryList = placeRepository.getFavourites()
                    currentfavoritesUiState.copy(places = placeSummaryList)
                } catch (e: InternetException) {
                    currentfavoritesUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.resources.getString(R.string.error_message_no_forecast)

                    )
                } catch (e: Exception) {
                    currentfavoritesUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.resources.getString(R.string.error_message_getting_favourites)
                    )
                }
            }
        }
    }

    fun toggleFavourite(place: PlaceInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (place.isFavourite) {
                placeRepository.unmakeFavourite(placeId = place.id)
                _favoritesUiState.update { currentFavouritesUiState ->
                    currentFavouritesUiState.copy(
                        places = currentFavouritesUiState.places.map {
                            if (it.id == place.id) {
                                it.copy(isFavourite = false)
                            } else {
                                it.copy()
                            }
                        }
                    )
                }
            } else {
                placeRepository.makeFavourite(placeId = place.id)
                _favoritesUiState.update { currentFavouritesUiState ->
                    currentFavouritesUiState.copy(
                        places = currentFavouritesUiState.places.map {
                            if (it.id == place.id) {
                                it.copy(isFavourite = true)
                            } else {
                                it.copy()
                            }
                        }
                    )
                }

            }
        }
    }

    /**
     * Set showSnackbar to false, so when the snackbar refresh it will be shown again
     */
    fun snackbarDismissed() {
        _favoritesUiState.update { currentfavoritesUiState ->
            currentfavoritesUiState.copy(
                showSnackbar = false,
            )
        }
    }

    /**
     *  This function refresh loadPlaceInfo when you use snackbar in favouritescreen:
     */
    fun refreshList() {
        _favoritesUiState.update { currentfavoritesUiState ->
            currentfavoritesUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch(Dispatchers.IO) {
            loadList()
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return FavoritesViewModel(
                    placeRepository = (application as ApplicationSkumring).dbRepository,
                    context = application.context
                ) as T
            }
        }
    }
}



package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeInfoDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoDao
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoEntity

/*
class PlaceInfoDatabaseViewModel(private val placeDao: PlaceInfoDao) : ViewModel() {
    val allPlaces: LiveData<List<PlaceInfoEntity>> = placeDao.getAllPlaces().asLiveData()
    fun insert(place: PlaceInfoEntity) = viewModelScope.launch {
        placeDao.insert(place)
    }

    fun set_favorite(placeId: Long) = viewModelScope.launch {
        placeDao.markAsFavorite(placeId)
    }

    fun remove_favorite(placeId: Long) = viewModelScope.launch {
        placeDao.unmarkAsFavorite(placeId)
    }
}


 */
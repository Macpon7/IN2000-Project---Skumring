package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


//Sjekk
//https://www.youtube.com/watch?v=7cEqDV_c94k&list=PLrJS8IW7z9HHA5-Giy9-Z8akJ461pn6ds&index=6
class PlaceInfodbViewModel(private val placeInfoRepository: PlaceInfoRepository) : ViewModel() {

    //fun getAllPlaces() = databaseRepository.getAllPlaces().asLiveData(viewModelScope.coroutineContext)
    //fun getAllPlaces() = databaseRepository.getAllPlaces() //skal denne annoteres med livedata

    fun addPlace(id: Int, name: String, lat: String, long: String) = viewModelScope.launch{
        //opprett objekt
        val placeObject = PlaceInfoEntity(id, name, lat, long)
        placeInfoRepository.insertCustomPlace(placeObject)
    }

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    //val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()
}


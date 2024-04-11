package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.launch


//Sjekk
//https://www.youtube.com/watch?v=7cEqDV_c94k&list=PLrJS8IW7z9HHA5-Giy9-Z8akJ461pn6ds&index=6
class PlaceInfodbViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    fun getAllPlaces() = databaseRepository.getAllPlaces().asLiveData(viewModelScope.coroutineContext)

    /*
    fun addPlace(place: infoooo) = viewModelScope.launch{
        //opprett objekt
        val placeObject = PlaceInfoEntity(inpuuut)

    }

     */
}


package no.uio.ifi.in2000.adrianch.adrianch.skumring

import android.app.Application
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.AppDatabase
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlacesRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlacesRepositoryImpl

public class ApplicationSkumring: Application() {
    lateinit var dbRepository: PlacesRepository
    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getDatabase(this.applicationContext)

        dbRepository = PlacesRepositoryImpl(
            placeInfoDao = db.placeInfoDao(),
            forecastDao = db.forecastDao(),
            imageDao = db.imageDao()
        )
    }
}
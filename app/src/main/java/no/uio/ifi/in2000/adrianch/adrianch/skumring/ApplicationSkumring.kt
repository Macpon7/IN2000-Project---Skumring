package no.uio.ifi.in2000.adrianch.adrianch.skumring

import android.app.Application
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.AppDatabase
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepositoryImpl

public class ApplicationSkumring: Application() {
    lateinit var dbRepository: PlaceRepository
    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getDatabase(this.applicationContext)

        dbRepository = PlaceRepositoryImpl(
            placeInfoDao = db.placeInfoDao(),
            forecastDao = db.forecastDao(),
            imageDao = db.imageDao()
        )
    }
}
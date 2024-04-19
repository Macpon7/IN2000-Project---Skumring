package no.uio.ifi.in2000.adrianch.adrianch.skumring

import android.app.Application
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.AppDatabase
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoRepositoryImpl

public class ApplicationSkumring: Application() {
    lateinit var dbRepository: PlaceInfoRepository
    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getDatabase(this.applicationContext)

        dbRepository = PlaceInfoRepositoryImpl(
            placeInfoDao = db.placeInfoDao(),
            forecastDao = db.forecastDao(),
            imageDao = db.imageDao()
        )
    }
}
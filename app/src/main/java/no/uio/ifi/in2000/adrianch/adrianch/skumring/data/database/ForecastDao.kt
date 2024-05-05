package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ForecastDao {
    @Insert
    fun insertForecasts(forecasts: List<ForecastEntity>)

    @Upsert
    fun upsertForecasts(forecasts: List<ForecastEntity>)

    @Query("SELECT * FROM forecasts WHERE place_id = :placeId")
    fun getForecasts(placeId: Int): List<ForecastEntity>

    @Query("DELETE FROM forecasts WHERE place_id = :placeId")
    fun deleteForecasts(placeId: Int)
}
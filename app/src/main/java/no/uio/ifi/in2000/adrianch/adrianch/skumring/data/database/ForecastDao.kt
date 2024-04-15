package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ForecastDao {
    @Insert
    suspend fun insertForecasts(forecasts: List<ForecastEntity>)

    @Update
    suspend fun updateForecasts(forecasts: List<ForecastEntity>)

    @Query("SELECT * FROM forecasts WHERE place_id = :placeId")
    suspend fun getForecasts(placeId: Int): List<ForecastEntity>
}
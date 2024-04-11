package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceInfoDao {
    @Insert
    suspend fun insert(place: PlaceInfoEntity)

    @Update
    suspend fun update(place: PlaceInfoEntity)

    @Query("SELECT * FROM placeInfo")
    fun getAllPlaces(): Flow<List<PlaceInfoEntity>>
    //fun getAllPlaces(): List<PlaceInfoEntity>
    //
    //



    /*
    @Query("UPDATE placeInfo SET favorite = 1 WHERE id = :placeId")
    suspend fun markAsFavorite(placeId: Long)

    @Query("UPDATE placeInfo SET favorite = 0 WHERE id = :placeId")
    suspend fun unmarkAsFavorite(placeId: Long)

     */
}
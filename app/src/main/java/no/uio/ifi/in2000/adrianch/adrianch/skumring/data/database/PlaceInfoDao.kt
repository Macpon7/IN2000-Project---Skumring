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
    // for tester: fun getAllPlaces(): List<PlaceInfoEntity>

    //Features vi kan implementere etter hvert
    /*
    @Query("UPDATE placeInfo SET isFavorite = True WHERE id = :placeId")
    suspend fun markAsFavorite(placeId: Long)

    @Query("UPDATE placeInfo SET isFavorite = False WHERE id = :placeId")
    suspend fun unmarkAsFavorite(placeId: Long)

     */
}
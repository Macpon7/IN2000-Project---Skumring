package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaceInfoDao {
    @Insert
    suspend fun insert(place: PlaceInfoEntity)

    @Update
    suspend fun update(place: PlaceInfoEntity)

    @Query("SELECT * FROM placeInfo")
    fun getAllPlaces(): List<PlaceInfoEntity>
    // for tester: fun getAllPlaces(): List<PlaceInfoEntity>



    @Query("SELECT is_custom_place FROM placeInfo WHERE id = :placeId")
    suspend fun checkIfCustomPlace(placeId: Int): Int?

    @Query("DELETE FROM placeInfo WHERE id= :placeId")
    suspend fun deleteCustomPlace(placeId: Int)

    @Query("UPDATE placeInfo SET is_custom_place = 1 WHERE id = :placeId")
    suspend fun markAsFavorite(placeId: Int)

    @Query("UPDATE placeInfo SET is_favourite = 0 WHERE id = :placeId")
    suspend fun unmarkAsFavorite(placeId: Int)


}
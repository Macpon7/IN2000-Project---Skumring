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
    fun getAllPlaces(): List<PlaceInfoEntity>
    // for tester: fun getAllPlaces(): List<PlaceInfoEntity>

    //@Query("SELECT * FROM placeInfo WHERE id = :placeId")
    //suspend fun getPlace(placeId: Int)

    @Query("SELECT * FROM placeInfo WHERE id=:placeId")
    fun getOnePlace(placeId: Int): Flow<PlaceInfoEntity>

    @Query("SELECT * FROM placeInfo WHERE is_favourite = 1")
    fun getFavourites(): Flow<List<PlaceInfoEntity>>

    @Query("SELECT * FROM placeInfo WHERE is_custom_place = 1")
    fun getCustomPlaces(): Flow<List<PlaceInfoEntity>>

    //@Insert(onConflict = OnConflictStrategy.REPLACE) use this?
    @Insert
    fun insertCustomPlace(place: PlaceInfoEntity)

    @Query("SELECT is_custom_place FROM placeInfo WHERE id = :placeId")
    fun checkIfCustomPlace(placeId: Int): Flow<Boolean>

    @Query("DELETE FROM placeInfo WHERE id= :placeId")
    fun deleteCustomPlace(placeId: Int)

    @Query("UPDATE placeInfo SET is_favourite = 1 WHERE id = :placeId")
    fun markAsFavorite(placeId: Int)

    @Query("UPDATE placeInfo SET is_favourite = 0 WHERE id = :placeId")
    fun unmarkAsFavorite(placeId: Int)

}

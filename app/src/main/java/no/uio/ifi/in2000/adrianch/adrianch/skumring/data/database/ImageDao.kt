package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert
    fun insert(image: List<ImageEntity>)

    @Query("SELECT * FROM images WHERE place_id = :placeId")
    fun getImages(placeId: Int): Flow<List<ImageEntity>>
}
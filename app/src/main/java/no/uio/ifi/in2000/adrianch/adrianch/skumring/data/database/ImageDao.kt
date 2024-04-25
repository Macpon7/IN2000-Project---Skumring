package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Insert
    fun insertSingleImage(image: ImageEntity)

    @Insert
    fun insert(image: List<ImageEntity>)

    @Query("SELECT img_path FROM images WHERE place_id = :place_id")
    suspend fun checkDefaultImage(place_id: Int): String

    @Query("SELECT * FROM images WHERE place_id = :placeId")
    fun getImages(placeId: Int): List<ImageEntity>
}
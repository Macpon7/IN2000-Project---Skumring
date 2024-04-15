package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert
    suspend fun insert(image: Array<ImageEntity>)

    @Query("SELECT * FROM images WHERE place_id = :placeId")
    suspend fun getImages(placeId: Int): Array<ImageEntity>
}
package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {

    @Insert
    fun insertImage(image: ImageEntity)

    @Query("SELECT * FROM images WHERE place_id = :placeId")
    fun getImages(placeId: Int): List<ImageEntity>

    @Query("DELETE FROM images WHERE place_id = :placeId")
    fun deleteImages(placeId: Int)
}
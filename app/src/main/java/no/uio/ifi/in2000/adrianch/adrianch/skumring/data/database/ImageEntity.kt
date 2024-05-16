package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "images", foreignKeys = [ForeignKey(
        entity = PlaceInfoEntity::class, parentColumns = ["id"], childColumns = ["place_id"]
    )]
)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "place_id") var placeId: Int,
    @ColumnInfo(name = "img_path") var imgPath: String,
    @ColumnInfo(defaultValue = "2024-04-20") var timestamp: LocalDate
)
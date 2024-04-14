package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "placeInfo")
data class PlaceInfoEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var description: String,
    var latitude: String,
    var longitude: String,
    @ColumnInfo(name = "is_custom_place") var isCustomPlace: Boolean,
    @ColumnInfo(name = "is_favourite") var isFavourite: Boolean
)
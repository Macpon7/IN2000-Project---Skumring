package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "placeInfo")
data class PlaceInfoEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var name: String,
    var lat: String,
    var long: String,
    @ColumnInfo(name = "is_custom_place") var isCustomPlace: Int,
    @ColumnInfo(name = "is_favourite") var isFavourite: Int
)
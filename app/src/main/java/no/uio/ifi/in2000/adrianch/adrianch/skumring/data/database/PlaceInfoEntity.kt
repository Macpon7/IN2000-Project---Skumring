package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "placeInfo")
data class PlaceInfoEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "",
    var lat: String = "",
    var long: String = ""
    //var isFavorite : Boolean = false //doesnt make sense to have a
)
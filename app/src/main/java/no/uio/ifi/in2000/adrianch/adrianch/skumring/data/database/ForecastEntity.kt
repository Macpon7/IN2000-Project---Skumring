package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import java.time.LocalDateTime

@Entity(
    tableName = "forecasts",
    foreignKeys = [
        ForeignKey(entity = PlaceInfoEntity::class, parentColumns = ["id"], childColumns = ["place_id"])
    ],
    indices = [Index("place_id")]
)
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "place_id") var placeId: Int,
    @ColumnInfo(name = "sunset_date_time") var sunsetDateTime: LocalDateTime,
    @ColumnInfo(name = "weather_rating") var weatherRating: WeatherConditionsRating,
    @ColumnInfo(name = "cloud_condition_low") var cloudConditionLow: CloudConditions,
    @ColumnInfo(name = "cloud_condition_medium") var cloudConditionMedium: CloudConditions,
    @ColumnInfo(name = "cloud_condition_high") var cloudConditionHigh: CloudConditions,
    @ColumnInfo(name = "air_condition") var airCondition: AirConditions,
    @ColumnInfo(name = "sunset_temp") var sunsetTemp: String,
    @ColumnInfo(name = "weather_icon") var weatherIcon: String,
    @ColumnInfo(name = "blue_hour_time") var blueHourDateTime: LocalDateTime,
    @ColumnInfo(name = "golden_hour_time") var goldenHourDateTime: LocalDateTime,
    var timestamp: LocalDateTime
)
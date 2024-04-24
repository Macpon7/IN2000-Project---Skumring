package no.uio.ifi.in2000.adrianch.adrianch.skumring.locationforecast

import com.google.gson.Gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.forecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.LocationForecastInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherPerHour
import org.junit.Test
import java.time.LocalDateTime

class LocationForecastDataSourceTest {

    /**
     * Tests that the conversion from JSON data structure to our own WeatherPerHour objects works correctly,
     * using a dummy JSON string contained in the file LocationForecastDummyData.kt which is part of the same
     * package as this test file
     */
    @Test
    fun dataClassesMatchValuesInJSON() {
        val gson = Gson()

        val decodedResponse: LocationForecastInfo = gson.fromJson(
            shortTestData,
            LocationForecastInfo::class.java)

        val expected = listOf(
            WeatherPerHour(
                //"2024-03-08T12:00:00Z"
                time = LocalDateTime.of(2024, 3, 8, 12, 0),
                instant = WeatherDetails(
                    air_pressure_at_sea_level = 1027.1,
                    air_temperature = 2.3,
                    air_temperature_percentile_10 = 1.9,
                    air_temperature_percentile_90 = 2.8,
                    cloud_area_fraction = 8.6,
                    cloud_area_fraction_high = 0.0,
                    cloud_area_fraction_low = 0.0,
                    cloud_area_fraction_medium = 0.0,
                    dew_point_temperature = -4.2,
                    fog_area_fraction = 0.0,
                    relative_humidity = 63.3,
                    ultraviolet_index_clear_sky = 1.8,
                    wind_from_direction = 81.7,
                    wind_speed = 1.6,
                    wind_speed_of_gust = 4.1,
                    wind_speed_percentile_10 = 1.5,
                    wind_speed_percentile_90= 1.9
                ),
                icon="clearsky_day"
            ),
            WeatherPerHour(
                //"2024-03-11T00:00:00Z"
                time = LocalDateTime.of(2024, 3,11, 0, 0, 0),
                instant = WeatherDetails(
                    air_pressure_at_sea_level = 1022.7,
                    air_temperature = -0.1,
                    air_temperature_percentile_10 = -0.6,
                    air_temperature_percentile_90 = 0.2,
                    cloud_area_fraction = 100.0,
                    cloud_area_fraction_high = 0.4,
                    cloud_area_fraction_low = 100.0,
                    cloud_area_fraction_medium = 5.9,
                    dew_point_temperature = -1.7,
                    fog_area_fraction = 0.0,
                    relative_humidity = 89.9,
                    ultraviolet_index_clear_sky = 0.0,
                    wind_from_direction = 19.1,
                    wind_speed = 2.0,
                    wind_speed_of_gust = 0.0,
                    wind_speed_percentile_10 = 1.7,
                    wind_speed_percentile_90 = 2.5
                ),
                icon="cloudy"
            )
        )

        val source = LocationForecastDataSource()
        val result = source.convertResponseToWeatherPerHour(decodedResponse)

        assert(expected == result)
    }
}
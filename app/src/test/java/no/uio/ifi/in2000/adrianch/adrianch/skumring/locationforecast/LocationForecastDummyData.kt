package no.uio.ifi.in2000.adrianch.adrianch.skumring.locationforecast

val shortTestData = """{
  "type": "Feature",
  "geometry": {
    "type": "Point",
    "coordinates": [
      10,
      60,
      139
    ]
  },
  "properties": {
    "meta": {
      "updated_at": "2024-03-08T11:38:34Z",
      "units": {
        "air_pressure_at_sea_level": "hPa",
        "air_temperature": "celsius",
        "air_temperature_max": "celsius",
        "air_temperature_min": "celsius",
        "air_temperature_percentile_10": "celsius",
        "air_temperature_percentile_90": "celsius",
        "cloud_area_fraction": "%",
        "cloud_area_fraction_high": "%",
        "cloud_area_fraction_low": "%",
        "cloud_area_fraction_medium": "%",
        "dew_point_temperature": "celsius",
        "fog_area_fraction": "%",
        "precipitation_amount": "mm",
        "precipitation_amount_max": "mm",
        "precipitation_amount_min": "mm",
        "probability_of_precipitation": "%",
        "probability_of_thunder": "%",
        "relative_humidity": "%",
        "ultraviolet_index_clear_sky": "1",
        "wind_from_direction": "degrees",
        "wind_speed": "m/s",
        "wind_speed_of_gust": "m/s",
        "wind_speed_percentile_10": "m/s",
        "wind_speed_percentile_90": "m/s"
      }
    },
    "timeseries": [
      {
        "time": "2024-03-08T12:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 1027.1,
              "air_temperature": 2.3,
              "air_temperature_percentile_10": 1.9,
              "air_temperature_percentile_90": 2.8,
              "cloud_area_fraction": 8.6,
              "cloud_area_fraction_high": 0,
              "cloud_area_fraction_low": 0,
              "cloud_area_fraction_medium": 0,
              "dew_point_temperature": -4.2,
              "fog_area_fraction": 0,
              "relative_humidity": 63.3,
              "ultraviolet_index_clear_sky": 1.8,
              "wind_from_direction": 81.7,
              "wind_speed": 1.6,
              "wind_speed_of_gust": 4.1,
              "wind_speed_percentile_10": 1.5,
              "wind_speed_percentile_90": 1.9
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "clearsky_day",
              "symbol_confidence": "certain"
            },
            "details": {
              "probability_of_precipitation": 0
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "clearsky_day"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0,
              "probability_of_thunder": 0
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "clearsky_day"
            },
            "details": {
              "air_temperature_max": 3.2,
              "air_temperature_min": -2.3,
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0
            }
          }
        }
      },
      {
        "time": "2024-03-11T00:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 1022.7,
              "air_temperature": -0.1,
              "air_temperature_percentile_10": -0.6,
              "air_temperature_percentile_90": 0.2,
              "cloud_area_fraction": 100,
              "cloud_area_fraction_high": 0.4,
              "cloud_area_fraction_low": 100,
              "cloud_area_fraction_medium": 5.9,
              "dew_point_temperature": -1.7,
              "relative_humidity": 89.9,
              "wind_from_direction": 19.1,
              "wind_speed": 2,
              "wind_speed_percentile_10": 1.7,
              "wind_speed_percentile_90": 2.5
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "cloudy",
              "symbol_confidence": "certain"
            },
            "details": {
              "probability_of_precipitation": 7.8
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "air_temperature_max": -0.1,
              "air_temperature_min": -1.4,
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 5.9
            }
          }
        }
      }
    ]
  }
}"""
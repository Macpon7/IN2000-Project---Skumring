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

val goodWeatherTestData = """
{
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
      "updated_at": "2024-02-24T14:34:28Z",
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
        "time": "2024-02-24T15:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 997.5,
              "air_temperature": 4.3,
              "air_temperature_percentile_10": 3.4,
              "air_temperature_percentile_90": 5.1,
              "cloud_area_fraction": 3.1,
              "cloud_area_fraction_high": 76.6,
              "cloud_area_fraction_low": 41,
              "cloud_area_fraction_medium": 0.5,
              "dew_point_temperature": 1.7,
              "fog_area_fraction": 0,
              "relative_humidity": 84,
              "ultraviolet_index_clear_sky": 0.2,
              "wind_from_direction": 218.6,
              "wind_speed": 2.9,
              "wind_speed_of_gust": 6.6,
              "wind_speed_percentile_10": 2.1,
              "wind_speed_percentile_90": 4.2
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 60.7
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0,
              "probability_of_thunder": 0.2
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "air_temperature_max": 2.9,
              "air_temperature_min": -0.8,
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 5.9
            }
          }
        }
      },
      {
        "time": "2024-02-24T16:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 997.7,
              "air_temperature": 2.9,
              "air_temperature_percentile_10": 2,
              "air_temperature_percentile_90": 4,
              "cloud_area_fraction": 10,
              "cloud_area_fraction_high": 97.2,
              "cloud_area_fraction_low": 30.6,
              "cloud_area_fraction_medium": 34.3,
              "dew_point_temperature": 1.3,
              "fog_area_fraction": 0,
              "relative_humidity": 89.4,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 221,
              "wind_speed": 2.2,
              "wind_speed_of_gust": 5.7,
              "wind_speed_percentile_10": 1.7,
              "wind_speed_percentile_90": 3.7
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 64.8
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0,
              "probability_of_thunder": 0.1
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "air_temperature_max": 1.4,
              "air_temperature_min": -0.8,
              "precipitation_amount": 0,
              "precipitation_amount_max": 0.7,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 16.4
            }
          }
        }
      },
      {
        "time": "2024-02-24T17:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 998.1,
              "air_temperature": 1.4,
              "air_temperature_percentile_10": 0.4,
              "air_temperature_percentile_90": 2.5,
              "cloud_area_fraction": 15.8,
              "cloud_area_fraction_high": 76.9,
              "cloud_area_fraction_low": 40.2,
              "cloud_area_fraction_medium": 51.5,
              "dew_point_temperature": 0.4,
              "fog_area_fraction": 0,
              "relative_humidity": 94.4,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 239.5,
              "wind_speed": 2.1,
              "wind_speed_of_gust": 4.4,
              "wind_speed_percentile_10": 1.8,
              "wind_speed_percentile_90": 3.2
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "cloudy",
              "symbol_confidence": "uncertain"
            },
            "details": {
              "probability_of_precipitation": 66
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0,
              "probability_of_thunder": 0.2
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "air_temperature_max": 1.5,
              "air_temperature_min": -0.8,
              "precipitation_amount": 0,
              "precipitation_amount_max": 1.1,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 27.4
            }
          }
        }
      },
      {
        "time": "2024-02-24T18:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 998.5,
              "air_temperature": 0.6,
              "air_temperature_percentile_10": -0.7,
              "air_temperature_percentile_90": 1.8,
              "cloud_area_fraction": 12.5,
              "cloud_area_fraction_high": 91.6,
              "cloud_area_fraction_low": 48.5,
              "cloud_area_fraction_medium": 16.1,
              "dew_point_temperature": -0.3,
              "fog_area_fraction": 0,
              "relative_humidity": 95.2,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 261.6,
              "wind_speed": 2.3,
              "wind_speed_of_gust": 4.6,
              "wind_speed_percentile_10": 1.4,
              "wind_speed_percentile_90": 2.9
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 70.2
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0,
              "probability_of_thunder": 0.1
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "lightsleet"
            },
            "details": {
              "air_temperature_max": 1.5,
              "air_temperature_min": -0.8,
              "precipitation_amount": 0.6,
              "precipitation_amount_max": 2.2,
              "precipitation_amount_min": 0.1,
              "probability_of_precipitation": 44
            }
          }
        }
      },
      {
        "time": "2024-02-24T19:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 998.8,
              "air_temperature": -0.8,
              "air_temperature_percentile_10": -1.8,
              "air_temperature_percentile_90": 0.5,
              "cloud_area_fraction": 12.3,
              "cloud_area_fraction_high": 65.6,
              "cloud_area_fraction_low": 76.6,
              "cloud_area_fraction_medium": 14.4,
              "dew_point_temperature": -1.3,
              "fog_area_fraction": 1.2,
              "relative_humidity": 97.7,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 329.7,
              "wind_speed": 1.3,
              "wind_speed_of_gust": 4.4,
              "wind_speed_percentile_10": 1,
              "wind_speed_percentile_90": 2.3
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 76.3
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 2.3,
              "probability_of_thunder": 0.2
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "lightsleet"
            },
            "details": {
              "air_temperature_max": 1.5,
              "air_temperature_min": -0.6,
              "precipitation_amount": 0.6,
              "precipitation_amount_max": 3.1,
              "precipitation_amount_min": 0.1,
              "probability_of_precipitation": 49.1
            }
          }
        }
      },
      {
        "time": "2024-02-24T20:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 999.1,
              "air_temperature": -0.6,
              "air_temperature_percentile_10": -2.2,
              "air_temperature_percentile_90": 0.2,
              "cloud_area_fraction": 12.5,
              "cloud_area_fraction_high": 84.8,
              "cloud_area_fraction_low": 97.4,
              "cloud_area_fraction_medium": 60.8,
              "dew_point_temperature": -1.1,
              "fog_area_fraction": 29,
              "relative_humidity": 97.8,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 333.5,
              "wind_speed": 1.1,
              "wind_speed_of_gust": 2.7,
              "wind_speed_percentile_10": 0.9,
              "wind_speed_percentile_90": 1.5
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 78
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "fog"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0.3,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 20.5,
              "probability_of_thunder": 0.3
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "lightsleet"
            },
            "details": {
              "air_temperature_max": 1.5,
              "air_temperature_min": 0.5,
              "precipitation_amount": 0.6,
              "precipitation_amount_max": 3.3,
              "precipitation_amount_min": 0.1,
              "probability_of_precipitation": 52.8
            }
          }
        }
      }
    ]
  }
}
"""

val badWeatherTestData = """
{
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
      "updated_at": "2024-02-24T14:34:28Z",
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
        "time": "2024-02-24T15:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 997.5,
              "air_temperature": 4.3,
              "air_temperature_percentile_10": 3.4,
              "air_temperature_percentile_90": 5.1,
              "cloud_area_fraction": 92.1,
              "cloud_area_fraction_high": 76.6,
              "cloud_area_fraction_low": 41,
              "cloud_area_fraction_medium": 0.5,
              "dew_point_temperature": 1.7,
              "fog_area_fraction": 0,
              "relative_humidity": 84,
              "ultraviolet_index_clear_sky": 0.2,
              "wind_from_direction": 218.6,
              "wind_speed": 2.9,
              "wind_speed_of_gust": 6.6,
              "wind_speed_percentile_10": 2.1,
              "wind_speed_percentile_90": 4.2
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 60.7
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0,
              "probability_of_thunder": 0.2
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "air_temperature_max": 2.9,
              "air_temperature_min": -0.8,
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 5.9
            }
          }
        }
      },
      {
        "time": "2024-02-24T16:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 997.7,
              "air_temperature": 2.9,
              "air_temperature_percentile_10": 2,
              "air_temperature_percentile_90": 4,
              "cloud_area_fraction": 100,
              "cloud_area_fraction_high": 97.2,
              "cloud_area_fraction_low": 30.6,
              "cloud_area_fraction_medium": 34.3,
              "dew_point_temperature": 1.3,
              "fog_area_fraction": 0,
              "relative_humidity": 89.4,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 221,
              "wind_speed": 2.2,
              "wind_speed_of_gust": 5.7,
              "wind_speed_percentile_10": 1.7,
              "wind_speed_percentile_90": 3.7
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 64.8
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0,
              "probability_of_thunder": 0.1
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "air_temperature_max": 1.4,
              "air_temperature_min": -0.8,
              "precipitation_amount": 0,
              "precipitation_amount_max": 0.7,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 16.4
            }
          }
        }
      },
      {
        "time": "2024-02-24T17:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 998.1,
              "air_temperature": 1.4,
              "air_temperature_percentile_10": 0.4,
              "air_temperature_percentile_90": 2.5,
              "cloud_area_fraction": 92.8,
              "cloud_area_fraction_high": 76.9,
              "cloud_area_fraction_low": 40.2,
              "cloud_area_fraction_medium": 51.5,
              "dew_point_temperature": 0.4,
              "fog_area_fraction": 0,
              "relative_humidity": 94.4,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 239.5,
              "wind_speed": 2.1,
              "wind_speed_of_gust": 4.4,
              "wind_speed_percentile_10": 1.8,
              "wind_speed_percentile_90": 3.2
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "cloudy",
              "symbol_confidence": "uncertain"
            },
            "details": {
              "probability_of_precipitation": 66
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0,
              "probability_of_thunder": 0.2
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "air_temperature_max": 1.5,
              "air_temperature_min": -0.8,
              "precipitation_amount": 0,
              "precipitation_amount_max": 1.1,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 27.4
            }
          }
        }
      },
      {
        "time": "2024-02-24T18:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 998.5,
              "air_temperature": 0.6,
              "air_temperature_percentile_10": -0.7,
              "air_temperature_percentile_90": 1.8,
              "cloud_area_fraction": 96.5,
              "cloud_area_fraction_high": 91.6,
              "cloud_area_fraction_low": 48.5,
              "cloud_area_fraction_medium": 16.1,
              "dew_point_temperature": -0.3,
              "fog_area_fraction": 0,
              "relative_humidity": 95.2,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 261.6,
              "wind_speed": 2.3,
              "wind_speed_of_gust": 4.6,
              "wind_speed_percentile_10": 1.4,
              "wind_speed_percentile_90": 2.9
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 70.2
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 0,
              "probability_of_thunder": 0.1
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "lightsleet"
            },
            "details": {
              "air_temperature_max": 1.5,
              "air_temperature_min": -0.8,
              "precipitation_amount": 0.6,
              "precipitation_amount_max": 2.2,
              "precipitation_amount_min": 0.1,
              "probability_of_precipitation": 44
            }
          }
        }
      },
      {
        "time": "2024-02-24T19:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 998.8,
              "air_temperature": -0.8,
              "air_temperature_percentile_10": -1.8,
              "air_temperature_percentile_90": 0.5,
              "cloud_area_fraction": 95.3,
              "cloud_area_fraction_high": 65.6,
              "cloud_area_fraction_low": 76.6,
              "cloud_area_fraction_medium": 14.4,
              "dew_point_temperature": -1.3,
              "fog_area_fraction": 1.2,
              "relative_humidity": 97.7,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 329.7,
              "wind_speed": 1.3,
              "wind_speed_of_gust": 4.4,
              "wind_speed_percentile_10": 1,
              "wind_speed_percentile_90": 2.3
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 76.3
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "cloudy"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 2.3,
              "probability_of_thunder": 0.2
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "lightsleet"
            },
            "details": {
              "air_temperature_max": 1.5,
              "air_temperature_min": -0.6,
              "precipitation_amount": 0.6,
              "precipitation_amount_max": 3.1,
              "precipitation_amount_min": 0.1,
              "probability_of_precipitation": 49.1
            }
          }
        }
      },
      {
        "time": "2024-02-24T20:00:00Z",
        "data": {
          "instant": {
            "details": {
              "air_pressure_at_sea_level": 999.1,
              "air_temperature": -0.6,
              "air_temperature_percentile_10": -2.2,
              "air_temperature_percentile_90": 0.2,
              "cloud_area_fraction": 99.5,
              "cloud_area_fraction_high": 84.8,
              "cloud_area_fraction_low": 97.4,
              "cloud_area_fraction_medium": 60.8,
              "dew_point_temperature": -1.1,
              "fog_area_fraction": 29,
              "relative_humidity": 97.8,
              "ultraviolet_index_clear_sky": 0,
              "wind_from_direction": 333.5,
              "wind_speed": 1.1,
              "wind_speed_of_gust": 2.7,
              "wind_speed_percentile_10": 0.9,
              "wind_speed_percentile_90": 1.5
            }
          },
          "next_12_hours": {
            "summary": {
              "symbol_code": "lightsleet",
              "symbol_confidence": "somewhat certain"
            },
            "details": {
              "probability_of_precipitation": 78
            }
          },
          "next_1_hours": {
            "summary": {
              "symbol_code": "fog"
            },
            "details": {
              "precipitation_amount": 0,
              "precipitation_amount_max": 0.3,
              "precipitation_amount_min": 0,
              "probability_of_precipitation": 20.5,
              "probability_of_thunder": 0.3
            }
          },
          "next_6_hours": {
            "summary": {
              "symbol_code": "lightsleet"
            },
            "details": {
              "air_temperature_max": 1.5,
              "air_temperature_min": 0.5,
              "precipitation_amount": 0.6,
              "precipitation_amount_max": 3.3,
              "precipitation_amount_min": 0.1,
              "probability_of_precipitation": 52.8
            }
          }
        }
      }
    ]
  }
}
"""
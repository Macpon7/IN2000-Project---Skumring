package no.uio.ifi.in2000.adrianch.adrianch.skumring.goldenhourbluehour

val osloTestCall = """ {
    "results": {
        "date": "2024-05-02",
        "sunrise": "6:09:48 AM",
        "sunset": "8:02:47 PM",
        "first_light": "4:28:05 AM",
        "last_light": "9:44:30 PM",
        "dawn": "5:40:54 AM",
        "dusk": "8:31:41 PM",
        "solar_noon": "1:06:18 PM",
        "golden_hour": "7:25:50 PM",
        "day_length": "13:52:58",
        "timezone": "America/New_York",
        "utc_offset": -240
    },
    "status": "OK"
    }
"""

val northPoleTestCall = """{
    "results": {
        "date": "2000-01-01",
        "sunrise": null,
        "sunset": null,
        "first_light": null,
        "last_light": null,
        "dawn": null,
        "dusk": null,
        "solar_noon": "1:04:01 PM",
        "golden_hour": null,
        "day_length": "NaN:NaN:NaN",
        "timezone": "CET",
        "utc_offset": 60
    },
    "status": "OK"
}
"""
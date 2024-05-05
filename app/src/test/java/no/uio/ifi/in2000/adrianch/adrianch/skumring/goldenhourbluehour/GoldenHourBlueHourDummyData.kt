package no.uio.ifi.in2000.adrianch.adrianch.skumring.goldenhourbluehour

val osloTestCall = """ {
    "results": {
        "date":"2000-01-01",
        "sunrise":"9:23:45 AM",
        "sunset":"3:25:11 PM",
        "first_light":"6:39:21 AM",
        "last_light":"6:09:35 PM",
        "dawn":"8:26:57 AM",
        "dusk":"4:22:00 PM",
        "solar_noon":"12:24:28 PM",
        "golden_hour":"1:26:27 PM",
        "day_length":"6:01:25",
        "timezone":"CET",
        "utc_offset":60
    },
    "status":"OK"
}"""

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
}"""
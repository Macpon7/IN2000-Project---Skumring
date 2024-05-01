package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating

/**
 * A check that visualizes weather conditions by displaying appropriate icons based on the provided weather condition string
 */
@Composable
fun WeatherIconCheck(weatherCondition: String, weather: WeatherConditionsRating) {
    //If its thunder
    if (weatherCondition.contains("thunder")) {
        Icon(
            painterResource(id = R.drawable.thunder_icon),
            contentDescription = stringResource(id = R.string.weather_icon_thunder),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
        //If it rains
    } else if (weatherCondition.contains("rain")) {
        Icon(
            painterResource(id = R.drawable.rain_icon),
            contentDescription = stringResource(id = R.string.weather_icon_rain),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
        //If its snowy
    } else if (weatherCondition.contains("snow")) {
        Icon(
            painterResource(id = R.drawable.snow_icon),
            contentDescription = stringResource(id = R.string.weather_icon_snow),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
        //If the weather is clear
    } else if (weatherCondition.contains("clear")) {
        Icon(
            painterResource(id = R.drawable.clear_icon),
            contentDescription = stringResource(id = R.string.weather_icon_clear),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )

        //If weatherconditions is excellent and there are some clouds high in the higher layers of the sky
    } else if (weather == WeatherConditionsRating.EXCELLENT && weatherCondition.contains("partly")) {
        Icon(
            painterResource(id = R.drawable.partly_cloudy_sun_icon),
            contentDescription = stringResource(id = R.string.weather_icon_cloudy_sun),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
        //if there are some clouds are lower in the sky
    } else if (weatherCondition.contains("partly")) {
        Icon(
            painterResource(id = R.drawable.partly_cloudy_icon),
            contentDescription = stringResource(id = R.string.weather_icon_partly_cloudy),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
        //If weatherconditons is excellent and there are clouds in the higher layers of the sky
    } else if (weather == WeatherConditionsRating.EXCELLENT && weatherCondition.contains("cloudy")) {
        Icon(
            painterResource(id = R.drawable.cloudy_sun_icon),
            contentDescription = stringResource(id = R.string.weather_icon_cloudy_sun),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
        //If there are clouds lower in the sky
    } else if (weatherCondition.contains("cloudy")) {
        Icon(
            painterResource(id = R.drawable.cloudy_icon),
            contentDescription = stringResource(id = R.string.weather_icon_cloudy),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
        //if the weather is fair
    } else if (weatherCondition.contains("fair")) {
        Icon(
            painterResource(id = R.drawable.fair_icon),
            contentDescription = stringResource(id = R.string.weather_icon_fair),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
        //if the weather is foggy
    } else if (weatherCondition.contains("fog")) {
        Icon(
            painterResource(id = R.drawable.fog_icon),
            contentDescription = stringResource(id = R.string.weather_icon_fog),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
        //If none of the other checks work, show default image
    } else {
        Icon(
            painterResource(id = R.drawable.image_not_found),
            contentDescription = stringResource(id = R.string.weather_icon_not_found),
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
    }
}
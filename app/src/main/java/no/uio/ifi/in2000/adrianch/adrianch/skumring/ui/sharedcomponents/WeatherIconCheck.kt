package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R


/**
 * A check that visualizes weather conditions by displaying appropriate icons based on the provided weather condition string
 */
@Composable
fun WeatherIconCheck (weatherCondition: String) {
    if (weatherCondition.contains("thunder")) {
        Icon(
            painterResource(id = R.drawable.thunder_icon),
            contentDescription = "Weather icon thunder",
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
    } else if (weatherCondition.contains("rain")) {
        Icon(
            painterResource(id = R.drawable.rain_icon),
            contentDescription = "Weather icon rain",
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
    } else if (weatherCondition.contains("snow")) {
        Icon(
            painterResource(id = R.drawable.snow_icon),
            contentDescription = "Weather icon snow",
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )

        } else if (weatherCondition.contains("clear")) {
        Icon(
            painterResource(id = R.drawable.clear_icon),
            contentDescription = "Weather icon clear",
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )

    } else if (weatherCondition.contains("partly")) {
        Icon(
            painterResource(id = R.drawable.partly_cloudy_icon),
            contentDescription = "Weather icon cloudy",
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
    } else if ( weatherCondition.contains("fair")) {
        Icon(
            painterResource(id = R.drawable.fair_icon),
            contentDescription = "Weather icon cloudy",
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
    }else if (weatherCondition.contains("cloudy")) {
            Icon(
                painterResource(id = R.drawable.cloudy_icon),
                contentDescription = "Weather icon cloudy",
                tint = Color.Unspecified,
                modifier = Modifier.size(140.dp)
            )
        }else if ( weatherCondition.contains("fog")) {
                Icon(
                    painterResource(id = R.drawable.fog_icon),
                    contentDescription = "Weather icon cloudy",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(140.dp)
                )
    } else {
        Icon ( //if none of the other checks work, show default image
            painterResource(id = R.drawable.image_not_found),
            contentDescription = "Weather icon cloudy",
            tint = Color.Unspecified,
            modifier = Modifier.size(140.dp)
        )
    }
}
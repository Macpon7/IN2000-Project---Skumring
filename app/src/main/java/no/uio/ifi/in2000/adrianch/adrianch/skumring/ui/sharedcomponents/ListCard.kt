package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme
import java.io.File
import java.time.LocalDateTime

/**
 * Cards with information about places
 */
@Composable
fun ListCard(
    place: PlaceInfo,
    onItemClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onDeleteClick: () -> Unit = {}
) {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onItemClick), //Click to infoscreen
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                //Box for picture:
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.LightGray)
                        .fillMaxWidth()
                ) {
                    if (place.images.isNotEmpty()) {
                        if (place.isCustomPlace) {
                            //this is for fetching/getting images that are uploaded into internal storage
                            val context = LocalContext.current
                            val imageFile = File(context.filesDir, place.images[0].path)
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current).data(imageFile)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            AsyncImage(
                                model = "file:///android_asset/presetImages/${place.images[0].path}",
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 8.dp)
                ) {
                    Text(
                        text = place.name,
                        modifier = Modifier.padding(vertical = 2.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Row {
                        if (place.isCustomPlace) {
                            IconButton(onClick = onDeleteClick) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        IconButton(onClick = onFavouriteClick) {
                            if (place.isFavourite) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.FavoriteBorder,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.secondary
                                )

                            }
                        }
                    }
                }
                Row(
                    //For displaying weather conditions and information popup
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(start = 15.dp, end = 12.dp, bottom = 8.dp)
                        .fillMaxWidth()
                ) {//Conditions at sunset
                    Text(
                        text = stringResource(R.string.weather_condition),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        //text changing based on weather conditions, in different textbox because of change of color
                        text = stringResource(id = place.sunEvents[0].conditions.weatherRating.stringResourceId),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,

                        )
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onItemClick), //Click to infoscreen
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                //Box for picture:
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.LightGray)
                        .fillMaxWidth()
                ) {
                    if (place.images.isNotEmpty()) {
                        if (place.isCustomPlace) {
                            //this is for fetching/getting images that are uploaded into internal storage
                            val context = LocalContext.current
                            val imageFile = File(context.filesDir, place.images[0].path)
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current).data(imageFile)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            AsyncImage(
                                model = "file:///android_asset/presetImages/${place.images[0].path}",
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 8.dp)
                    ) {
                        Text(
                            text = place.name,
                            modifier = Modifier.padding(vertical = 2.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Row {
                            if (place.isCustomPlace) {
                                IconButton(onClick = onDeleteClick) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            IconButton(onClick = onFavouriteClick) {
                                if (place.isFavourite) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.FavoriteBorder,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )

                                }
                            }
                        }
                    }
                    Row(
                        //For displaying weather conditions and information popup
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(start = 15.dp, end = 12.dp, bottom = 8.dp)
                            .fillMaxWidth()
                    ) {//Conditions at sunset
                        Text(
                            text = stringResource(R.string.weather_condition),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            //text changing based on weather conditions, in different textbox because of change of color
                            text = stringResource(id = place.sunEvents[0].conditions.weatherRating.stringResourceId),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Cards with information about places
 */
@Preview
@Composable
fun ListCardPreview() {
    SkumringTheme(useDarkTheme = true) {
        ListCard(place = PlaceInfo(
            id = 0,
            name = "Holmenkollen",
            description = "Et fantastisk fint sted å ta bilde av dine nære og kjære under en solnedgang som ikke kan sammenlignes med noe annet",
            lat = "",
            long = "",
            isFavourite = false,
            isCustomPlace = true,
            hasNotification = false,
            images = emptyList(),
            sunEvents = listOf(
                SunEvent(
                    time = LocalDateTime.now(),
                    tempAtEvent = "4.7",
                    weatherIcon = "suncloudy",
                    conditions = WeatherConditions(
                        weatherRating = WeatherConditionsRating.EXCELLENT,
                        cloudConditionLow = CloudConditions.CLEAR,
                        cloudConditionHigh = CloudConditions.CLEAR,
                        cloudConditionMedium = CloudConditions.CLEAR,
                        airCondition = AirConditions.LOW,
                    ),
                    blueHourTime = LocalDateTime.now(),
                    goldenHourTime = LocalDateTime.now()
                )
            )
        ), onItemClick = {}, onFavouriteClick = {})
    }
}

package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating

/**
 * Cards with information about places
 */
@Preview
@Composable
fun ListCardPreview() {
    ListCard(
        name = "Holmenkollen stadion",
        description = "Holmenkollen er et fantastisk fint sted å ta bilde av dine nære og kjære under en utrolig fin solnedgang",
        isFavourite = false,
        onItemClick = {},
        onFavouriteClick = {},
        weatherConditionsRating = WeatherConditionsRating.DECENT
    )
}

/**
 * Cards with information about places
 */
@Composable
fun ListCard(
    weatherConditionsRating: WeatherConditionsRating,
    name: String,
    description: String, isFavourite: Boolean,
    onItemClick: () -> Unit,
    onFavouriteClick: () -> Unit
) {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable(onClick = onItemClick) //Click to infoscreen
            ) {

                //Box for picture:
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.LightGray, RoundedCornerShape((0.dp)))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.place_display_placeholder),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box( //Box for content description
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                        )
                        {
                            Text(
                                text = name,
                                modifier = Modifier
                                    .padding(vertical = 2.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Row {
                                IconButton(onClick = { onFavouriteClick() }) {
                                    if (isFavourite) {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Filled.FavoriteBorder,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )

                                    }
                                }
                            }
                        }
                        Row(
                            //For displaying weather conditions and information popup
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {//Conditions at sunset
                            Text(
                                text = stringResource(R.string.weather_condition),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                //text changing based on weather conditions, in different textbox because of change of color
                                text = stringResource(id = weatherConditionsRating.stringResourceId),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        //Text for description
                        Text(
                            text = description,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier
                                .padding(vertical = 2.dp, horizontal = 8.dp)
                                .padding(bottom = 6.dp),
                        )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable(onClick = onItemClick) //Click to infoscreen
            ) {
                //Box for picture:
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.LightGray, RoundedCornerShape((0.dp)))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.place_display_placeholder),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box( //Box for content description
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp),
                        )
                        {
                            Text(
                                text = name,
                                modifier = Modifier
                                    .padding(vertical = 2.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Row {
                                IconButton(onClick = { onFavouriteClick() }) {
                                    if (isFavourite) {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Filled.FavoriteBorder,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            //For displaying weather conditions and information popup
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {//Conditions at sunset
                            Text(
                                text = stringResource(R.string.weather_condition),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                //text changing based on weather conditions, in different textbox because of change of color
                                text = stringResource(id = weatherConditionsRating.stringResourceId),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        //Text for description
                        Text(
                            text = description,
                            modifier = Modifier
                                .padding(vertical = 2.dp, horizontal = 10.dp)
                                .padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}
package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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

/**
 * Cards with information about places
 */
@Preview
@Composable
fun ListCardPreview () {
    ListCard(name = "Holmenkollen stadion", description = "Holmenkollen er et fantastisk fint sted å ta bilde av dine nære og kjære under en utrolig fin solnedgang", isFavourite = false, onItemClick = {}, onFavouriteClick = {})
}

/**
 * Cards with information about places
 */
@Composable
fun ListCard(name: String, description: String, isFavourite: Boolean,
             onItemClick: () -> Unit,
             onFavouriteClick: () -> Unit) {
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
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
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
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Row {
                            IconButton(onClick = { onFavouriteClick() }) {
                                if (isFavourite) {
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
                    //Text for description. Do we want weather condition in the future?
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 8.dp)
                            .padding(bottom = 6.dp)
                            .align(Alignment.BottomStart)
                    )
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
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
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
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Row {
                            IconButton(onClick = { onFavouriteClick() }) {
                                if (isFavourite) {
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
                    //Text for description. Do we want weather condition in the future?
                    Text(
                        text = description,
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 10.dp)
                            .padding(bottom = 8.dp)
                            .align(Alignment.BottomCenter),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
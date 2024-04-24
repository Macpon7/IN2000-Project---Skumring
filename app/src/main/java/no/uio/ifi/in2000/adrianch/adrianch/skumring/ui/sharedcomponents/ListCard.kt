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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ListCard {
}
/**
 * Cards with information about places
 */
@Composable
fun ListCard(name: String, description: String, onItemClick: () -> Unit) {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable(onClick = onItemClick), //Click to infoscreen
            ){

                //Box for picture:
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.LightGray, RoundedCornerShape((0.dp)))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Image Placeholder",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Row (
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
                        fontSize = 20.sp
                    )
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "")
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.Notifications, contentDescription = "")
                        }
                    }
                }

                //Text for description. Do we want weather condition in the future?
                Text(
                    text = description,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .padding(bottom = 4.dp)
                        .align(Alignment.CenterHorizontally))
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable(onClick = onItemClick), //Click to infoscreen
            ){

                //Box for picture:
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.LightGray, RoundedCornerShape((0.dp)))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Image Placeholder",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Row (
                    horizontalArrangement = Arrangement.Center,
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
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "")
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.Notifications, contentDescription = "")
                        }
                    }
                }

                //Text for description. Do we want weather condition in the future?
                Text(
                    text = description,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .padding(bottom = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination

object PlaceInfoScreenDestination : NavigationDestination {
    override val icon = null
    override val buttonTitle = null
    override val route = "infoscreen/{lat}/{long}/{id}"
    override val titleRes = null

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaceInfoScreen(
    placeViewModel: PlaceInfoViewModel = viewModel(),
    lat: String,
    long: String,
    id: Int,
    navController: NavController
) {

    LaunchedEffect(key1 = id) {
        placeViewModel.loadPlaceInfo(lat = lat, long = long, id = id)
    }

    val placeUiState: PlaceInfoUiState by placeViewModel.placeInfoUiState.collectAsState()

    var placename: String = placeUiState.placeInfo.name
    var description: String = placeUiState.placeInfo.description


    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(vertical = 10.dp) // Choose the size of the topbar
            ) {
                    IconButton(onClick = {navController.popBackStack()},
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Arrow back" ,
                            modifier = Modifier.size(30.dp) // Set the size of the Icon inside the IconButton
                        )
                    }
                Text(
                    text = "Infoskjerm ${placeUiState.placeInfo.name}",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray)
                    .padding(top = 64.dp) // Padding for topbar
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp) // Padding to show the grey box
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                    ) {
                        ContentInfoScreen(placeUiState.placeInfo.description)
                    }
                }
            }
        }
    )
}

/**
 * Function with alle the content of the homescreen
 * This exclude the top- and bottomBar
 */
@Composable
fun ContentInfoScreen(description: String) {
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        //Picture of the place:
        PlacePicture()

        //Add space between pictures and text
        Spacer(modifier = Modifier.height(20.dp))


        //Description of the place:
        Text(text = description)
        //Text(text = "På blindern er det dårlig vær")

        Spacer(modifier = Modifier.height(50.dp))

        //Sundown of the place:
        Text(text = "Sola går ned 18:30")
    }
}

/**
 * Picture of the place
 */
@Composable
fun PlacePicture() {
    Box(
        modifier = Modifier
            .width(500.dp)
            .height(400.dp)
            .padding(6.dp)
            .background(Color.LightGray, RoundedCornerShape((16.dp))),
    ) {
        Text(
            text = "Place Display Placeholder",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
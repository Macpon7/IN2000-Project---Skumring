package no.uio.ifi.in2000.adrianch.adrianch.skumring

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.AppDatabase
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.SkumringNavHost

@Composable
@Preview
fun SkumringApp(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val dbRepository: PlaceInfoRepository = PlaceInfoRepositoryImpl(
        placeInfoDao = db.placeInfoDao(),
        forecastDao = db.forecastDao(),
        imageDao = db.imageDao()
        )
    SkumringNavHost(
        navController = navController,
        placeInfoRepository = dbRepository,
        context = context)
}




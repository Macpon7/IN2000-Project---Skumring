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
    val db = AppDatabase.getDatabase(LocalContext.current)

    val dbRepository: PlaceInfoRepository = PlaceInfoRepositoryImpl(db.placeInfoDao())
    SkumringNavHost(navController = navController, placeInfoRepository = dbRepository)
}




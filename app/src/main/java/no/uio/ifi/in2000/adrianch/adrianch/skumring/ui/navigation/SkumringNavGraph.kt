package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeViewModel
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListViewModel
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo.PlaceInfoScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo.PlaceInfoScreenDestination

@Composable
fun SkumringNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    placeInfoRepository: PlaceInfoRepository,
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            //TODO mapListViewModel must be removed from HomeScreen in the future
            HomeScreen(homeViewModel = HomeViewModel(
                placeInfoRepository = placeInfoRepository,
                context = context),
                mapListViewModel = MapListViewModel(placeInfoRepository = placeInfoRepository),
                navController = navController)
        }
        composable(route = MapListDestination.route) {
            MapListScreen(mapListViewModel = MapListViewModel(placeInfoRepository = placeInfoRepository) , navController = navController)
        }
        composable(
            route = PlaceInfoScreenDestination.route,
            arguments = listOf(
                navArgument("lat") { type = NavType.StringType },
                navArgument("long") { type = NavType.StringType },
                navArgument("id") { type = NavType.IntType }
                )
            ) {backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")
            val long = backStackEntry.arguments?.getString("long")
            val id = backStackEntry.arguments?.getInt("id")
            if (lat != null && long != null && id != null) {
                PlaceInfoScreen(
                    navController = navController,
                    lat = lat,
                    long = long,
                    id = id
                )
            }
        }
    }
}
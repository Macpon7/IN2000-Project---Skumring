package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation

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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo.PlaceInfoViewModel

@Composable
fun SkumringNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    placeInfoRepository: PlaceInfoRepository
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(homeViewModel = HomeViewModel(placeInfoRepository = placeInfoRepository), navController = navController)
        }
        composable(route = MapListDestination.route) {
            MapListScreen(mapListViewModel = MapListViewModel(placeInfoRepository = placeInfoRepository) , navController = navController)
        }
        composable(
            route = PlaceInfoScreenDestination.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
                )
            ) {backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            if (id != null) {
                PlaceInfoScreen(
                    placeViewModel = PlaceInfoViewModel(placeInfoRepository = placeInfoRepository),
                    navController = navController,
                    id = id
                )
            }
        }
    }
}
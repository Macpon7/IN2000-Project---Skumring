package no.uio.ifi.in2000.adrianch.adrianch.skumring

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.TestRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.TestRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.SkumringNavHost

@Composable
@Preview
fun SkumringApp(
    navController: NavHostController = rememberNavController(),
    testRepository: TestRepository = TestRepositoryImpl()
) {
    SkumringNavHost(navController = navController, testRepo = testRepository)
}




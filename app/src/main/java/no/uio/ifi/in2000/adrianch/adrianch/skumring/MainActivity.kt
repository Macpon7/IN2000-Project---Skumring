package no.uio.ifi.in2000.adrianch.adrianch.skumring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel: MainViewModel by viewModels()

        installSplashScreen().apply{
            setKeepOnScreenCondition {
                mainViewModel.mainScreenUiState.value.splashScreenReady
            }
        }

        setContent {
            SkumringTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SkumringApp()
                }
            }
        }
    }
}
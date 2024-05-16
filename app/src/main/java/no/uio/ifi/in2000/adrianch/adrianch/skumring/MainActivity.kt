package no.uio.ifi.in2000.adrianch.adrianch.skumring

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel: MainViewModel by viewModels()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.mainScreenUiState.value.splashScreenReady
            }
        }

        setContent {
            val preferences =
                LocalContext.current.getSharedPreferences("user_settings", MODE_PRIVATE)
            val editor = preferences.edit()
            if (!preferences.contains("first_launch")) {
                editor.putBoolean("first_launch", true)
            }
            if (!preferences.contains("theme")) {
                editor.putString("theme", "system")
            }
            editor.apply()

            Log.d("Settings", preferences.all.toString())

            // Set theme according to user preferences
            val useDark: Boolean = if (preferences.getString("theme", "system") == "system") {
                isSystemInDarkTheme()
            } else {
                preferences.getString("theme", "system") == "dark"
            }

            SkumringTheme(useDarkTheme = useDark) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    SkumringApp()
                }
            }
        }
    }
}
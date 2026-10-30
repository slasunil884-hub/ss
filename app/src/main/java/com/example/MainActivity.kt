package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appcheck.configureAppCheck
import com.example.ui.screens.BootScreen
import com.example.ui.screens.MainHudScreen
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.KarenStarkOSTheme
import com.example.viewmodel.KarenOSViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: KarenOSViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory App Check configuration before any UI or Firebase operations
        configureAppCheck(this, intent)

        // STEP 2: Edge-to-edge layout
        enableEdgeToEdge()

        // STEP 3: Render Compose UI
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            KarenStarkOSTheme(activeTheme = uiState.activeTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CyberDarkBg
                ) {
                    Crossfade(
                        targetState = uiState.isBooted,
                        label = "BootCrossfade"
                    ) { isBooted ->
                        if (!isBooted) {
                            BootScreen(
                                viewModel = viewModel,
                                progress = uiState.bootProgress,
                                statusText = uiState.bootStatusText
                            )
                        } else {
                            MainHudScreen(
                                viewModel = viewModel,
                                uiState = uiState
                            )
                        }
                    }
                }
            }
        }
    }
}

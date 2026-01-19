package com.system.design

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.system.design.ui.main.OnboardingScreen
import com.system.design.ui.theme.SystemDesignTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure content is laid out edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            SystemDesignTheme {
                // Host nav directly as full screen content (no Scaffold padding)
                AppNavHost(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun AppNavHost(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboard",
        modifier = modifier
    ) {
        composable("onboard") {
            OnboardingScreen(
                onFinished = {
                    // navigate to main screen when ready
                }
            )
        }

    }
}

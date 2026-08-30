package com.whitenoisequran

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.ui.navigation.AppNavHost
import com.whitenoisequran.ui.navigation.Screen
import com.whitenoisequran.ui.theme.BackgroundNavy
import com.whitenoisequran.ui.theme.WhiteNoiseQuranTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val isOnboardingCompleted by appPreferences.isOnboardingCompletedFlow.collectAsStateWithLifecycle(initialValue = false)

            val startDestination = if (isOnboardingCompleted) {
                Screen.Main.route
            } else {
                Screen.Onboarding.route
            }

            WhiteNoiseQuranTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundNavy
                ) {
                    AppNavHost(startDestination = startDestination)
                }
            }
        }
    }
}

package com.whitenoisequran

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.service.AmbientSoundMixer
import com.whitenoisequran.service.AudioPlayerManager
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

    @Inject
    lateinit var audioPlayerManager: AudioPlayerManager

    @Inject
    lateinit var ambientSoundMixer: AmbientSoundMixer

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

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

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            audioPlayerManager.pause()
            ambientSoundMixer.stopAll()
        }
    }
}

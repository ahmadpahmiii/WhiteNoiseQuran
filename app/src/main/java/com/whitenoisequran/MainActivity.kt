package com.whitenoisequran

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.service.AmbientSoundMixer
import com.whitenoisequran.service.AudioPlayerManager
import com.whitenoisequran.ui.navigation.AppNavHost
import com.whitenoisequran.ui.navigation.Screen
import com.whitenoisequran.ui.theme.BackgroundNavy
import com.whitenoisequran.ui.theme.WhiteNoiseQuranTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var audioPlayerManager: AudioPlayerManager

    @Inject
    lateinit var ambientSoundMixer: AmbientSoundMixer

    companion object {
        /** Set by download notifications: open the download screen for this reciter id. */
        const val EXTRA_OPEN_DOWNLOADS_FOR_RECITER = "open_downloads_for_reciter"
    }

    private var openDownloadsFor by mutableStateOf<Int?>(null)

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

        // Only a fresh launch: a recreated activity's intent was already handled
        if (savedInstanceState == null) openDownloadsFor = intent.openDownloadsReciterId()

        setContent {
            val navController = rememberNavController()
            // Decide the first screen once. Re-deciding when onboarding completes would reset
            // navigation and throw the user from the Download screen to Main.
            val startDestination by produceState<String?>(initialValue = null) {
                value = if (appPreferences.isOnboardingCompletedFlow.first()) {
                    Screen.Main.route
                } else {
                    Screen.Onboarding.route
                }
            }

            WhiteNoiseQuranTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundNavy
                ) {
                    startDestination?.let {
                        AppNavHost(
                            startDestination = it,
                            navController = navController
                        )
                    }
                }
            }

            LaunchedEffect(openDownloadsFor, startDestination) {
                val reciterId = openDownloadsFor ?: return@LaunchedEffect
                if (startDestination == null) return@LaunchedEffect // graph not set yet
                navController.navigate(Screen.Download.createRoute(reciterId)) {
                    launchSingleTop = true
                }
                openDownloadsFor = null
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.openDownloadsReciterId()?.let { openDownloadsFor = it }
    }

    private fun Intent.openDownloadsReciterId() =
        getIntExtra(EXTRA_OPEN_DOWNLOADS_FOR_RECITER, -1).takeIf { it != -1 }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            audioPlayerManager.pause()
            ambientSoundMixer.stopAll()
        }
    }
}

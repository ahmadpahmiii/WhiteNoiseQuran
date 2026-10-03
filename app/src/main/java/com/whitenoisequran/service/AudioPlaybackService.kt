package com.whitenoisequran.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.whitenoisequran.MainActivity
import com.whitenoisequran.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AudioPlaybackService : MediaSessionService() {

    @Inject
    lateinit var audioPlayerManager: AudioPlayerManager

    @Inject
    lateinit var ambientSoundMixer: AmbientSoundMixer

    private var mediaSession: MediaSession? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    companion object {
        const val CHANNEL_ID = "quran_playback_channel"
        const val NOTIFICATION_ID = 1001

        /** Starts the service, which keeps playback going in the background. */
        fun start(context: Context) {
            try {
                context.startService(Intent(context, AudioPlaybackService::class.java))
            } catch (e: IllegalStateException) {
                e.printStackTrace() // app in the background: a running service carries on anyway
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        setMediaNotificationProvider(
            DefaultMediaNotificationProvider.Builder(this)
                .setChannelId(CHANNEL_ID)
                .setNotificationId(NOTIFICATION_ID)
                .build()
        )

        val sessionActivityIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val sessionActivityPendingIntent = PendingIntent.getActivity(
            this,
            0,
            sessionActivityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val session = MediaSession.Builder(this, audioPlayerManager.player)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
        mediaSession = session
        // Nothing binds a controller to this service, so without this Media3 never shows the
        // notification nor keeps playback in the foreground
        addSession(session)

        scope.launch {
            ambientSoundMixer.isPlaying.collect { ambientPlaying ->
                // The notification shows the surah, so rain alone loads it paused; loading refreshes it.
                // ponytail: offline with the current surah not downloaded there's nothing to load, so rain
                // alone isn't kept in the foreground; give the mix its own MediaSession if that matters.
                if (ambientPlaying && audioPlayerManager.loadCurrentSurah()) return@collect
                refreshNotification(session)
            }
        }
    }

    // Media3 keeps the service in the foreground only while the Quran plays; the ambient mix
    // playing alone has to count too, or Android stops the rain in the night.
    override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
        super.onUpdateNotification(
            session,
            startInForegroundRequired || ambientSoundMixer.isPlaying.value
        )
    }

    private fun refreshNotification(session: MediaSession) {
        val player = session.player
        val quranPlaying = player.playWhenReady &&
                (player.playbackState == Player.STATE_READY || player.playbackState == Player.STATE_BUFFERING)
        try {
            onUpdateNotification(session, quranPlaying)
        } catch (e: IllegalStateException) { // ForegroundServiceStartNotAllowedException (API 31+)
            e.printStackTrace()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_playback),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.channel_playback_desc)
                setShowBadge(true)
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        audioPlayerManager.pause()
        ambientSoundMixer.stopAll()
        stopSelf()
    }

    override fun onDestroy() {
        scope.cancel()
        mediaSession?.run {
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}

package com.whitenoisequran.domain.model

data class AmbientSound(
    val id: String,
    val name: String,
    val subtitle: String = "",
    val category: String = "Ambient",
    val iconEmoji: String,
    val iconDrawableName: String = "",
    val rawResName: String,
    val volume: Float = 0.5f,
    val isEnabled: Boolean = false,
    val sortOrder: Int = 0
) {
    companion object {
        val DefaultSounds = listOf(
            AmbientSound(
                id = "calming_rain",
                name = "Steady Rain",
                subtitle = "Deep, soothing downpour",
                category = "Rain",
                iconEmoji = "🌧️",
                iconDrawableName = "ic_sound_calming_rain",
                rawResName = "calming_rain",
                volume = 0.65f,
                isEnabled = false,
                sortOrder = 1
            ),
            AmbientSound(
                id = "soft_rain",
                name = "Gentle Drizzle",
                subtitle = "Light, peaceful droplets",
                category = "Rain",
                iconEmoji = "🌦️",
                iconDrawableName = "ic_sound_soft_rain",
                rawResName = "soft_rain",
                volume = 0.60f,
                isEnabled = false,
                sortOrder = 2
            ),
            AmbientSound(
                id = "rain_bird",
                name = "Rain & Songbirds",
                subtitle = "Morning drizzle with birds",
                category = "Nature",
                iconEmoji = "🐦",
                iconDrawableName = "ic_sound_rain_bird",
                rawResName = "rain_bird",
                volume = 0.55f,
                isEnabled = false,
                sortOrder = 3
            ),
            AmbientSound(
                id = "forest",
                name = "Lush Forest",
                subtitle = "Woodland breeze & trees",
                category = "Nature",
                iconEmoji = "🌲",
                iconDrawableName = "ic_sound_forest",
                rawResName = "forest",
                volume = 0.50f,
                isEnabled = false,
                sortOrder = 4
            ),
            AmbientSound(
                id = "ocean",
                name = "Ocean Waves",
                subtitle = "Rhythmic tide & calm surf",
                category = "Water",
                iconEmoji = "🌊",
                iconDrawableName = "ic_sound_ocean",
                rawResName = "ocean",
                volume = 0.50f,
                isEnabled = false,
                sortOrder = 5
            ),
            AmbientSound(
                id = "train",
                name = "Night Train",
                subtitle = "Rhythmic tracks for sleep",
                category = "Ambient",
                iconEmoji = "🚆",
                iconDrawableName = "ic_sound_train",
                rawResName = "train",
                volume = 0.45f,
                isEnabled = false,
                sortOrder = 6
            )
        )
    }
}

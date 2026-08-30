package com.whitenoisequran.domain.model

data class AmbientSound(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val rawResName: String,
    val volume: Float = 0.5f,
    val isEnabled: Boolean = false,
    val sortOrder: Int = 0
) {
    companion object {
        val DefaultSounds = listOf(
            AmbientSound(
                id = "white_noise",
                name = "White Noise",
                iconEmoji = "🌬",
                rawResName = "white_noise",
                volume = 0.65f,
                isEnabled = false,
                sortOrder = 1
            ),
            AmbientSound(
                id = "rain",
                name = "Rain",
                iconEmoji = "🌧",
                rawResName = "rain",
                volume = 0.80f,
                isEnabled = false,
                sortOrder = 2
            ),
            AmbientSound(
                id = "ocean",
                name = "Ocean",
                iconEmoji = "🌊",
                rawResName = "ocean",
                volume = 0.50f,
                isEnabled = false,
                sortOrder = 3
            ),
            AmbientSound(
                id = "forest",
                name = "Forest",
                iconEmoji = "🌿",
                rawResName = "forest",
                volume = 0.50f,
                isEnabled = false,
                sortOrder = 4
            )
        )
    }
}

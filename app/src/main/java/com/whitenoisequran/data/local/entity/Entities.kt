package com.whitenoisequran.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.whitenoisequran.domain.model.DownloadState

@Entity(tableName = "surahs", primaryKeys = ["number", "reciterId"])
data class SurahEntity(
    val number: Int,
    val reciterId: Int,
    val nameArabic: String,
    val nameLatin: String,
    val numberOfAyah: Int,
    val revelationType: String,
    val translationId: String,
    val downloadState: DownloadState = DownloadState.NONE,
    val localFilePath: String? = null,
    val audioDurationMs: Long = 0L
)

@Entity(tableName = "reciters")
data class ReciterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val nameArabic: String,
    val slug: String,
    val apiKey: String,
    val isPopular: Boolean = false,
    val avatarInitial: String = "Q"
)

@Entity(tableName = "ambient_sounds")
data class AmbientSoundEntity(
    @PrimaryKey val id: String,
    val name: String,
    val subtitle: String = "",
    val category: String = "Ambient",
    val iconEmoji: String,
    val iconDrawableName: String = "",
    val rawResName: String,
    val volume: Float = 0.5f,
    val isEnabled: Boolean = false,
    val sortOrder: Int = 0
)

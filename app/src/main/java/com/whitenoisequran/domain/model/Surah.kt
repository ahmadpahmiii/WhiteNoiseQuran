package com.whitenoisequran.domain.model

enum class DownloadState {
    NONE,
    DOWNLOADING,
    DONE,
    FAILED
}

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameLatin: String,
    val numberOfAyah: Int,
    val revelationType: String,
    val translationId: String,
    val downloadState: DownloadState = DownloadState.NONE,
    val localFilePath: String? = null,
    val audioDurationMs: Long = 0L
)

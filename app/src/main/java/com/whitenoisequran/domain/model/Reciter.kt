package com.whitenoisequran.domain.model

data class Reciter(
    val id: Int,
    val name: String,
    val nameArabic: String,
    val slug: String,
    val apiKey: String,
    val isPopular: Boolean = false,
    val avatarInitial: String = name.firstOrNull()?.toString() ?: "Q"
) {
    companion object {
        val DefaultReciters = listOf(
            Reciter(
                id = 1,
                name = "Abdullah Al-Juhany",
                nameArabic = "عبد الله الجهني",
                slug = "Abdullah-Al-Juhany",
                apiKey = "01",
                isPopular = false,
                avatarInitial = "ع"
            ),
            Reciter(
                id = 2,
                name = "Abdul Muhsin Al-Qasim",
                nameArabic = "عبد المحسن القاسم",
                slug = "Abdul-Muhsin-Al-Qasim",
                apiKey = "02",
                isPopular = false,
                avatarInitial = "ع"
            ),
            Reciter(
                id = 3,
                name = "Abdurrahman As-Sudais",
                nameArabic = "عبد الرحمن السديس",
                slug = "Abdurrahman-as-Sudais",
                apiKey = "03",
                isPopular = false,
                avatarInitial = "ع"
            ),
            Reciter(
                id = 4,
                name = "Ibrahim Al-Dossari",
                nameArabic = "إبراهيم الدوسري",
                slug = "Ibrahim-Al-Dossari",
                apiKey = "04",
                isPopular = false,
                avatarInitial = "إ"
            ),
            Reciter(
                id = 5,
                name = "Misyari Rasyid Al-Afasy",
                nameArabic = "مشاري راشد العفاسي",
                slug = "Misyari-Rasyid-Al-Afasi",
                apiKey = "05",
                isPopular = true,
                avatarInitial = "م"
            ),
            Reciter(
                id = 6,
                name = "Yasser Al-Dosari",
                nameArabic = "ياسر الدوسري",
                slug = "Yasser-Al-Dosari",
                apiKey = "06",
                isPopular = false,
                avatarInitial = "ي"
            )
        )
    }
}

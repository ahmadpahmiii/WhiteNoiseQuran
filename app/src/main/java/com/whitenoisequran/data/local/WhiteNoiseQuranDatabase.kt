package com.whitenoisequran.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.whitenoisequran.data.local.dao.AmbientSoundDao
import com.whitenoisequran.data.local.dao.ReciterDao
import com.whitenoisequran.data.local.dao.SurahDao
import com.whitenoisequran.data.local.entity.AmbientSoundEntity
import com.whitenoisequran.data.local.entity.ReciterEntity
import com.whitenoisequran.data.local.entity.SurahEntity

@Database(
    entities = [
        SurahEntity::class,
        ReciterEntity::class,
        AmbientSoundEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WhiteNoiseQuranDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun reciterDao(): ReciterDao
    abstract fun ambientSoundDao(): AmbientSoundDao
}

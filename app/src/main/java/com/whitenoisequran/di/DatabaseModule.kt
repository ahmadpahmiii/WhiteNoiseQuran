package com.whitenoisequran.di

import android.content.Context
import androidx.room.Room
import com.whitenoisequran.data.local.WhiteNoiseQuranDatabase
import com.whitenoisequran.data.local.dao.AmbientSoundDao
import com.whitenoisequran.data.local.dao.ReciterDao
import com.whitenoisequran.data.local.dao.SurahDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): WhiteNoiseQuranDatabase {
        return Room.databaseBuilder(
            context,
            WhiteNoiseQuranDatabase::class.java,
            "white_noise_quran.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSurahDao(database: WhiteNoiseQuranDatabase): SurahDao = database.surahDao()

    @Provides
    fun provideReciterDao(database: WhiteNoiseQuranDatabase): ReciterDao = database.reciterDao()

    @Provides
    fun provideAmbientSoundDao(database: WhiteNoiseQuranDatabase): AmbientSoundDao = database.ambientSoundDao()
}

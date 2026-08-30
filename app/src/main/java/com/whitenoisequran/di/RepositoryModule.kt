package com.whitenoisequran.di

import com.whitenoisequran.data.repository.AmbientRepositoryImpl
import com.whitenoisequran.data.repository.DownloadRepositoryImpl
import com.whitenoisequran.data.repository.QuranRepositoryImpl
import com.whitenoisequran.domain.repository.AmbientRepository
import com.whitenoisequran.domain.repository.DownloadRepository
import com.whitenoisequran.domain.repository.QuranRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuranRepository(impl: QuranRepositoryImpl): QuranRepository

    @Binds
    @Singleton
    abstract fun bindDownloadRepository(impl: DownloadRepositoryImpl): DownloadRepository

    @Binds
    @Singleton
    abstract fun bindAmbientRepository(impl: AmbientRepositoryImpl): AmbientRepository
}

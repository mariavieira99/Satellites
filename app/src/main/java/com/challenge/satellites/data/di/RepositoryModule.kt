/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.di

import com.challenge.satellites.data.api.TleApi
import com.challenge.satellites.data.repository.SatelliteRepositoryImpl
import com.challenge.satellites.domain.repository.SatelliteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideSatelliteRepository(api: TleApi): SatelliteRepository = SatelliteRepositoryImpl(api)
}
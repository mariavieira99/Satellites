/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.di

import android.content.Context
import androidx.room.Room
import com.challenge.satellites.data.api.TleApi
import com.challenge.satellites.data.local.SatelliteDatabase
import com.challenge.satellites.data.repository.SatelliteRepositoryImpl
import com.challenge.satellites.domain.repository.SatelliteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private const val BASE_URL = "https://tle.ivanstanojevic.me/api/"

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMusicApi(retrofit: Retrofit): TleApi {
        return retrofit.create(TleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SatelliteDatabase =
        Room.databaseBuilder(context, SatelliteDatabase::class.java, "satellite_db").build()


    @Provides
    @Singleton
    fun provideSatelliteRepository(api: TleApi, db: SatelliteDatabase): SatelliteRepository =
        SatelliteRepositoryImpl(api, db)
}
/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.repository

import android.util.Log
import com.challenge.satellites.data.api.TleApi
import com.challenge.satellites.data.model.SatelliteCollection
import com.challenge.satellites.domain.repository.SatelliteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "SatelliteRepositoryImpl"

class SatelliteRepositoryImpl @Inject constructor(private val api: TleApi) : SatelliteRepository {

    override suspend fun getSatellites(): SatelliteCollection? = withContext(Dispatchers.IO) {
        try {
            api.getCollection()
        } catch (e: Exception) {
            Log.e(TAG, "Exception caught=$e")
            return@withContext null
        }
    }

    override suspend fun getSatelliteById(satelliteId: Int): SatelliteCollection.Member? =
        withContext(Dispatchers.IO) {
            try {
                api.getSatelliteById(satelliteId)
            } catch (e: Exception) {
                Log.e(TAG, "Exception caught=$e")
                return@withContext null
            }
        }
}
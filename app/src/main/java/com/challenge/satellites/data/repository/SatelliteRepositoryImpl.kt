/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.repository

import android.util.Log
import com.challenge.satellites.data.api.ApiQueryParameters
import com.challenge.satellites.data.api.TleApi
import com.challenge.satellites.data.local.SatelliteDatabase
import com.challenge.satellites.data.local.SatelliteEntity
import com.challenge.satellites.data.mapper.toSatellite
import com.challenge.satellites.data.mapper.toSatelliteEntity
import com.challenge.satellites.domain.model.Satellite
import com.challenge.satellites.domain.repository.SatelliteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val TAG = "SatelliteRepositoryImpl"

class SatelliteRepositoryImpl @Inject constructor(
    private val api: TleApi,
    private val db: SatelliteDatabase
) : SatelliteRepository {

    override suspend fun getApiSatellites(queryParameters: ApiQueryParameters): List<Satellite> =
        withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getSatellites | queryParameters=$queryParameters")
            val satellitesDto = api.getCollection(
                satelliteQuery = queryParameters.sort,
                eccentricityGreaterOrEqual = queryParameters.eccentricityGreaterOrEqual,
                eccentricityLessOrEqual = queryParameters.eccentricityLessOrEqual,
                inclinationGreater = queryParameters.inclinationGreater,
                inclinationLess = queryParameters.inclinationLess
            )

            Log.d(TAG, "getSatellites | satellitesDtoSize=${satellitesDto.view}")
            val satellites = satellitesDto.member.map { it.toSatelliteEntity() }
            launch {
                Log.d(TAG, "insert into db")
                saveSatellitesToDb(satellites)
            }

            satellites.map { it.toSatellite() }

        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "Request timed out=$e")
            return@withContext emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Exception caught=$e")
            return@withContext emptyList()
        }
    }

    private suspend fun saveSatellitesToDb(satellites: List<SatelliteEntity>) {
        db.dao.insertAll(satellites)
        Log.d(TAG, "finished")
    }

    override suspend fun getSatelliteById(satelliteId: Int): Satellite? =
        withContext(Dispatchers.IO) {
            try {
                api.getSatelliteById(satelliteId).toSatellite()
            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "Request timed out=$e")
                return@withContext null
            } catch (e: Exception) {
                Log.e(TAG, "Exception caught=$e")
                return@withContext null
            }
        }
}
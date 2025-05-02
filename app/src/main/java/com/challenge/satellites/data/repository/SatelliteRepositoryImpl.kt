/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.repository

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.challenge.satellites.data.api.ApiQueryParameters
import com.challenge.satellites.data.api.TleApi
import com.challenge.satellites.data.local.SatelliteDatabase
import com.challenge.satellites.data.local.SatelliteEntity
import com.challenge.satellites.data.mapper.toSatellite
import com.challenge.satellites.data.mapper.toSatelliteEntity
import com.challenge.satellites.domain.model.Satellite
import com.challenge.satellites.domain.repository.SatelliteRepository
import com.challenge.satellites.presentation.EccentricityFilter
import com.challenge.satellites.presentation.InclinationFilter
import com.challenge.satellites.presentation.SatelliteSort
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

    // region API

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

    override suspend fun getApiSatelliteById(satelliteId: Int) = withContext(Dispatchers.IO) {
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

    // endregion

    // region DB

    override suspend fun getDbSatellites(
        sort: SatelliteSort,
        inclinationFilter: InclinationFilter,
        eccentricityFilter: EccentricityFilter,
    ): List<Satellite> {
        if (sort == SatelliteSort.DEFAULT && inclinationFilter == InclinationFilter.ANY && eccentricityFilter == EccentricityFilter.ANY) {
            Log.d(TAG, "getDbSatellites | retrieve default items")
            return db.dao.getAllSatellites().map { it.toSatellite() }
        }
        val query = buildQuery(sort, inclinationFilter, eccentricityFilter)
        val satellites = db.dao.getFilteredSatellites(query)
        Log.d(TAG, "getDbSatellites | satellites=${satellites.size}")
        return satellites.map { it.toSatellite() }
    }

    private fun buildQuery(
        sort: SatelliteSort,
        inclinationFilter: InclinationFilter,
        eccentricityFilter: EccentricityFilter,
    ): SupportSQLiteQuery {
        val queryBuilder = StringBuilder("SELECT * FROM satellites WHERE 1 = 1")

        when (inclinationFilter) {
            InclinationFilter.ANY -> {
                // Do nothing
            }

            InclinationFilter.LT_20 -> {
                inclinationFilter.lessThan?.let {
                    queryBuilder.append(" AND inclination < $it")
                }
            }

            InclinationFilter.BETWEEN_20_60, InclinationFilter.BETWEEN_60_100 -> {
                inclinationFilter.range?.let {
                    queryBuilder.append(" AND inclination BETWEEN ${it.start} AND ${it.endInclusive}")
                }
            }

            InclinationFilter.GT_100 -> {
                inclinationFilter.greaterThan?.let {
                    queryBuilder.append(" AND inclination > $it")
                }
            }
        }

        when (eccentricityFilter) {
            EccentricityFilter.ANY -> {
                // Do nothing
            }

            EccentricityFilter.LE_001 -> {
                eccentricityFilter.lessThanOrEqual?.let {
                    queryBuilder.append(" AND eccentricity <= $it")
                }
            }

            EccentricityFilter.RANGE_001_029, EccentricityFilter.RANGE_03_069, EccentricityFilter.RANGE_07_099 -> {
                eccentricityFilter.range?.let {
                    queryBuilder.append(" AND eccentricity > ${it.start} AND eccentricity <= ${it.endInclusive}")
                }
            }
        }

        sort.let { queryBuilder.append(" ORDER BY ${it.label} ASC") }

        queryBuilder.append(" LIMIT 35")

        return SimpleSQLiteQuery(queryBuilder.toString()).also {
            Log.d(TAG, "query=${it.sql}")
        }
    }

    private suspend fun saveSatellitesToDb(satellites: List<SatelliteEntity>) {
        db.dao.insertAll(satellites)
        Log.d(TAG, "finished")
    }

    override suspend fun getDbSatelliteById(satelliteId: Int): Satellite? {
        return db.dao.getSatelliteById(satelliteId)?.toSatellite()
    }

    // endregion
}
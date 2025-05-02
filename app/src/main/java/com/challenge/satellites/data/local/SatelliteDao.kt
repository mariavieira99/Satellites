/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface SatelliteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(satellites: List<SatelliteEntity>)

    @Query("SELECT * FROM satellites ORDER BY name ASC LIMIT 35")
    suspend fun getAllSatellites(): List<SatelliteEntity>

    @RawQuery(observedEntities = [SatelliteEntity::class])
    suspend fun getFilteredSatellites(query: SupportSQLiteQuery): List<SatelliteEntity>

    @Query("SELECT * FROM satellites WHERE satelliteId = :satelliteId")
    suspend fun getSatelliteById(satelliteId: Int): SatelliteEntity?
}
/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SatelliteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(satellites: List<SatelliteEntity>)

    @Query("SELECT * FROM satellites")
    suspend fun getAllSatellites(): List<SatelliteEntity>

    @Query("DELETE FROM satellites")
    suspend fun clearAll()
}
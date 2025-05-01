/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SatelliteEntity::class],
    version = 1
)
abstract class SatelliteDatabase : RoomDatabase() {
    abstract val dao: SatelliteDao
}
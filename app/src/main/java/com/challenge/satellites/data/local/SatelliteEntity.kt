/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "satellites")
data class SatelliteEntity(
    @PrimaryKey
    val satelliteId: Int,
    val date: String,
    val id: String,
    val line2: String,
    val line1: String,
    val name: String,
    val type: String,
    val inclination: Double,
    val eccentricity: Double
)
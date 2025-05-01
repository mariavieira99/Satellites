/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.mapper

import com.challenge.satellites.data.api.SatelliteCollection
import com.challenge.satellites.data.local.SatelliteEntity
import com.challenge.satellites.domain.model.Satellite

fun SatelliteCollection.Member.toSatelliteEntity(): SatelliteEntity {
    val (inclination, eccentricity) = parseTleLine2(line2)
    return SatelliteEntity(
        satelliteId = satelliteId,
        date = date,
        id = id,
        line2 = line2,
        line1 = line1,
        name = name,
        type = type,
        inclination = inclination,
        eccentricity = eccentricity
    )
}

fun SatelliteCollection.Member.toSatellite(): Satellite {
    val (inclination, eccentricity) = parseTleLine2(line2)
    return Satellite(
        satelliteId = satelliteId,
        date = date,
        id = id,
        line2 = line2,
        line1 = line1,
        name = name,
        type = type,
        inclination = inclination,
        eccentricity = eccentricity,
    )
}

private fun parseTleLine2(line2: String): Pair<Double, Double> {
    val parts = line2.trim().split(Regex("\\s+"))
    val inclination = parts[2].toDouble()
    val eccentricity = "0.${parts[4]}".toDouble()

    return Pair(inclination, eccentricity)
}

fun SatelliteEntity.toSatellite(): Satellite {
    return Satellite(
        satelliteId = satelliteId,
        date = date,
        id = id,
        line2 = line2,
        line1 = line1,
        name = name,
        type = type,
        inclination = inclination,
        eccentricity = eccentricity,
    )
}
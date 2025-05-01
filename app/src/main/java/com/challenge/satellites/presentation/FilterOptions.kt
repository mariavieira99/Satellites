/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation

import com.challenge.satellites.data.api.SatelliteSort


object FilterOptions {
    val eccentricityOptions = listOf(
        "Any" to null,
        "Circular (≤ 0.01)" to Pair(0.0, 0.01),
        "Low elliptical (0.01–0.29)" to Pair(0.01, 0.3),
        "Medium elliptical (0.3–0.69)" to Pair(0.3, 0.69),
        "High elliptical (0.7 - 0.99)" to Pair(0.8, 1.0),
    )

    val inclinationOptions = listOf(
        "Any" to null,
        "< 20°" to Pair(null, 20.0),
        "20–60°" to Pair(20.0, 60.0),
        "60–100°" to Pair(60.0, 100.0),
        "> 100°" to Pair(100.0, null)
    )

    val sortOptions = listOf(
        SatelliteSort.DEFAULT.value to null,
        SatelliteSort.INCLINATION.value to null,
        SatelliteSort.ECCENTRICITY.value to null,
    )
}
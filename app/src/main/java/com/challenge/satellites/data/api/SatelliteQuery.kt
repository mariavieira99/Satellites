/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.api

enum class SatelliteSort(val value: String) {
    INCLINATION("inclination"),
    ECCENTRICITY("eccentricity"),
    DEFAULT("name")
}

data class QueryParameters(
    val sort: String = SatelliteSort.DEFAULT.value,
    val eccentricityGreaterOrEqual: String? = null,
    val eccentricityLessOrEqual: String? = null,
    val inclinationGreater: String? = null,
    val inclinationLess: String? = null,
)
/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.api

import com.challenge.satellites.presentation.SatelliteSort

data class ApiQueryParameters(
    val sort: String = SatelliteSort.DEFAULT.label,
    val eccentricityGreaterOrEqual: String? = null,
    val eccentricityLessOrEqual: String? = null,
    val inclinationGreater: String? = null,
    val inclinationLess: String? = null,
)
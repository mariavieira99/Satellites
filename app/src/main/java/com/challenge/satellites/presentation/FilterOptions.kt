/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation

enum class InclinationFilter(
    override val label: String,
    val range: ClosedFloatingPointRange<Double>? = null,
    val greaterThan: Double? = null,
    val lessThan: Double? = null,
) : FilterOption {
    ANY("Any"),
    LT_20("< 20°", lessThan = 20.0),
    BETWEEN_20_60("20–60°", range = 20.0..60.0),
    BETWEEN_60_100("60–100°", range = 60.0..100.0),
    GT_100("> 100°", greaterThan = 100.0)
}

enum class EccentricityFilter(
    override val label: String,
    val range: ClosedFloatingPointRange<Double>? = null,
    val lessThanOrEqual: Double? = null
) : FilterOption {
    ANY("Any"),
    LE_001("Circular (≤ 0.01)", lessThanOrEqual = 0.01),
    RANGE_001_029("Low elliptical (0.01–0.29)", range = 0.01..0.29),
    RANGE_03_069("Medium elliptical (0.3–0.69)", range = 0.3..0.69),
    RANGE_07_099("High elliptical (0.7 - 0.99)", range = 0.7..0.99)
}

enum class SatelliteSort(override val label: String) : FilterOption {
    INCLINATION("inclination"),
    ECCENTRICITY("eccentricity"),
    DEFAULT("name")
}

interface FilterOption {
    val label: String
}
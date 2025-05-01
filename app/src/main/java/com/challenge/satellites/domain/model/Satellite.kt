/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.domain.model

data class Satellite(
    val date: String,
    val id: String,
    val line2: String,
    val line1: String,
    val name: String,
    val satelliteId: Int,
    val type: String,
    val inclination: Double,
    val eccentricity: Double
)
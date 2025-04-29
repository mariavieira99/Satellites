/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.model

import com.google.gson.annotations.SerializedName

data class SatelliteCollection(
    @SerializedName("@context") val context: String = "",
    @SerializedName("@id") val id: String = "",
    @SerializedName("member") val member: List<Member> = listOf(),
    @SerializedName("parameters") val parameters: Parameters = Parameters(),
    @SerializedName("totalItems") val totalItems: Int = 0,
    @SerializedName("@type") val type: String = "",
    @SerializedName("view") val view: View = View()
) {
    data class Member(
        @SerializedName("offset") val date: String = "",
        @SerializedName("@id") val id: String = "",
        @SerializedName("line2") val line2: String = "",
        @SerializedName("line1") val line1: String = "",
        @SerializedName("name") val name: String = "",
        @SerializedName("satelliteId") val satelliteId: Int = 0,
        @SerializedName("@type") val type: String = ""
    )

    data class Parameters(
        @SerializedName("page") val page: Int = 0,
        @SerializedName("page-size") val pageSize: Int = 0,
        @SerializedName("search") val search: String = "",
        @SerializedName("sort") val sort: String = "",
        @SerializedName("sort-dir") val sortDir: String = ""
    )

    data class View(
        @SerializedName("first") val first: String = "",
        @SerializedName("@id") val id: String = "",
        @SerializedName("last") val last: String = "",
        @SerializedName("next") val next: String = "",
        @SerializedName("@type") val type: String = ""
    )
}
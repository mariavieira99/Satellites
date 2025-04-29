/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.satellites.data.model.SatelliteCollection
import com.challenge.satellites.domain.usecase.GetSatelliteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SatelliteViewModel"

@HiltViewModel
class SatelliteViewModel @Inject constructor(
    private val satelliteUseCase: GetSatelliteUseCase,
) : ViewModel() {

    private val _satelliteData = MutableStateFlow<SatelliteCollection?>(null)
    val satelliteData: StateFlow<SatelliteCollection?> = _satelliteData

    init {
        viewModelScope.launch {
            try {
                val satellites = satelliteUseCase.invoke() ?: return@launch
                _satelliteData.value = satellites
            } catch (e: Exception) {
                Log.e(TAG, "e=$e")
            }
        }
    }

}
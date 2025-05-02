package com.challenge.satellites.presentation.viewmodel

import android.util.Log
import com.challenge.satellites.data.api.ApiQueryParameters
import com.challenge.satellites.domain.model.Satellite
import com.challenge.satellites.domain.usecase.GetSatelliteUseCase
import com.challenge.satellites.presentation.EccentricityFilter
import com.challenge.satellites.presentation.InclinationFilter
import com.challenge.satellites.presentation.SatelliteSort
import com.challenge.satellites.presentation.state.SatelliteCollectionUiState
import com.challenge.satellites.utils.NetworkConnectivityProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SatelliteViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: SatelliteViewModel
    private lateinit var mockkUseCase: GetSatelliteUseCase

    private val satelliteISS = Satellite(
        date = "2025-04-29T04:34:06+00:00",
        id = "https://tle.ivanstanojevic.me/api/tle/25544",
        line2 = "2 25544  51.6352 189.7367 0002491  81.0639 279.0631 15.49383308507563",
        line1 = "1 25544U 98067A   25119.19035294  .00013779  00000+0  25440-3 0  9995",
        name = "ISS (ZARYA)",
        satelliteId = 25544,
        type = "Tle",
        inclination = 51.6352,
        eccentricity = 0.0002491,
    )

    private val satelliteSWIFT = Satellite(
        date = "2025-05-01T22:30:48+00:00",
        id = "https://tle.ivanstanojevic.me/api/tle/28485",
        line2 = "2 28485  18.5564  31.4632 0005388 125.6481 234.4461 15.36622984123119",
        line1 = "1 28485U 04047A   25121.93806390  .00032478  00000+0  85683-3 0  9997",
        name = "SWIFT",
        satelliteId = 28485,
        type = "Tle",
        inclination = 18.5564,
        eccentricity = 0.0005388,
    )

    private val satellitesList = listOf(satelliteISS, satelliteSWIFT)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        mockkUseCase = mockk(relaxed = true)
        mockkObject(NetworkConnectivityProvider)
    }

    @Test
    fun `initial retrieve api items success should update satelliteCollectionUiState StateFlow`() =
        runTest {
            // Arrange
            coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
            every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)

            // Act
            viewModel = SatelliteViewModel(mockkUseCase)
            advanceUntilIdle()

            // Assert
            assertEquals(
                SatelliteCollectionUiState.Success(satellitesList),
                viewModel.satelliteCollectionUiState.value
            )
            coVerify { mockkUseCase.getApiSatellites(ApiQueryParameters()) }
        }

    @Test
    fun `initial retrieve api items empty should update satelliteCollectionUiState StateFlow`() =
        runTest {
            // Arrange
            coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns emptyList()
            every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)

            // Act
            viewModel = SatelliteViewModel(mockkUseCase)
            advanceUntilIdle()

            // Assert
            assertEquals(
                SatelliteCollectionUiState.Error,
                viewModel.satelliteCollectionUiState.value
            )
            coVerify { mockkUseCase.getApiSatellites(ApiQueryParameters()) }
        }


    @Test
    fun `initial retrieve api items error should update satelliteCollectionUiState StateFlow`() =
        runTest {
            // Arrange
            coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } throws Exception()
            every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)

            // Act
            viewModel = SatelliteViewModel(mockkUseCase)
            advanceUntilIdle()

            // Assert
            assertEquals(
                SatelliteCollectionUiState.Error,
                viewModel.satelliteCollectionUiState.value
            )
            coVerify { mockkUseCase.getApiSatellites(ApiQueryParameters()) }
        }

    @Test
    fun `initial retrieve db items success should update satelliteCollectionUiState StateFlow`() =
        runTest {
            // Arrange
            coEvery {
                mockkUseCase.getDbSatellites(
                    SatelliteSort.DEFAULT,
                    InclinationFilter.ANY,
                    EccentricityFilter.ANY
                )
            } returns satellitesList
            every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(false)

            // Act
            viewModel = SatelliteViewModel(mockkUseCase)
            advanceUntilIdle()

            // Assert
            assertEquals(
                SatelliteCollectionUiState.Success(satellitesList),
                viewModel.satelliteCollectionUiState.value
            )
            coVerify {
                mockkUseCase.getDbSatellites(
                    SatelliteSort.DEFAULT,
                    InclinationFilter.ANY,
                    EccentricityFilter.ANY
                )
            }
        }

    @Test
    fun `initial retrieve db items empty should update satelliteCollectionUiState StateFlow`() =
        runTest {
            // Arrange
            coEvery {
                mockkUseCase.getDbSatellites(
                    SatelliteSort.DEFAULT,
                    InclinationFilter.ANY,
                    EccentricityFilter.ANY
                )
            } returns emptyList()
            every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(false)

            // Act
            viewModel = SatelliteViewModel(mockkUseCase)
            advanceUntilIdle()

            // Assert
            assertEquals(
                SatelliteCollectionUiState.Error,
                viewModel.satelliteCollectionUiState.value
            )
            coVerify {
                mockkUseCase.getDbSatellites(
                    SatelliteSort.DEFAULT,
                    InclinationFilter.ANY,
                    EccentricityFilter.ANY
                )
            }
        }

    @Test
    fun `getFilteredItems method from API returns correct order`() = runTest {
        // Arrange
        val queryParameters = ApiQueryParameters(inclinationLess = "20.0")
        coEvery { mockkUseCase.getApiSatellites(queryParameters) } returns listOf(satelliteSWIFT)
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.inclinationOptionSelected = InclinationFilter.LT_20

        // Act
        viewModel.getFilteredItems()
        advanceUntilIdle()

        // Assert
        assertEquals(
            SatelliteCollectionUiState.Success(listOf(satelliteSWIFT)),
            viewModel.satelliteCollectionUiState.value
        )
        coVerify { mockkUseCase.getApiSatellites(queryParameters) }
    }

    @Test
    fun `getFilteredItems method from database returns correct order`() = runTest {
        // Arrange
        coEvery {
            mockkUseCase.getDbSatellites(
                SatelliteSort.DEFAULT,
                InclinationFilter.LT_20,
                EccentricityFilter.ANY
            )
        } returns listOf(satelliteSWIFT)
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(false)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.inclinationOptionSelected = InclinationFilter.LT_20

        // Act
        viewModel.getFilteredItems()
        advanceUntilIdle()

        // Assert
        assertEquals(
            SatelliteCollectionUiState.Success(listOf(satelliteSWIFT)),
            viewModel.satelliteCollectionUiState.value
        )
        coVerify {
            mockkUseCase.getDbSatellites(
                SatelliteSort.DEFAULT,
                InclinationFilter.LT_20,
                EccentricityFilter.ANY
            )
        }
    }

    @Test
    fun `getQueryParameters any filters method returns correct ApiQueryParameters`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)

        // Act
        val queryParameters = viewModel.getQueryParameters()

        // Assert
        val expected = ApiQueryParameters(
            sort = SatelliteSort.DEFAULT.label,
            eccentricityGreaterOrEqual = null,
            eccentricityLessOrEqual = null,
            inclinationGreater = null,
            inclinationLess = null,
        )
        assertEquals(expected, queryParameters)
    }

    @Test
    fun `getQueryParameters other filters method returns correct ApiQueryParameters`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.sortOptionSelected = SatelliteSort.INCLINATION
        viewModel.eccentricityOptionSelected = EccentricityFilter.RANGE_07_099
        viewModel.inclinationOptionSelected = InclinationFilter.BETWEEN_20_60

        // Act
        val queryParameters = viewModel.getQueryParameters()

        val expected = ApiQueryParameters(
            sort = SatelliteSort.INCLINATION.label,
            eccentricityGreaterOrEqual = "0.7",
            eccentricityLessOrEqual = "0.99",
            inclinationGreater = "20.0",
            inclinationLess = "60.0",
        )
        // Assert
        assertEquals(expected, queryParameters)
    }

    @Test
    fun `eccentricity any filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)

        // Act
        val filter = viewModel.getEccentricityFilter()

        // Assert
        assertEquals(Pair(null, null), filter)
    }

    @Test
    fun `eccentricity le_001 filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.eccentricityOptionSelected = EccentricityFilter.LE_001

        // Act
        val filter = viewModel.getEccentricityFilter()

        // Assert
        assertEquals(Pair(null, 0.01), filter)
    }

    @Test
    fun `eccentricity range_001_029 filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.eccentricityOptionSelected = EccentricityFilter.RANGE_001_029

        // Act
        val filter = viewModel.getEccentricityFilter()

        // Assert
        assertEquals(Pair(0.01, 0.29), filter)
    }

    @Test
    fun `eccentricity range_03_069 filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.eccentricityOptionSelected = EccentricityFilter.RANGE_03_069

        // Act
        val filter = viewModel.getEccentricityFilter()

        // Assert
        assertEquals(Pair(0.3, 0.69), filter)
    }

    @Test
    fun `eccentricity range_07_099 filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.eccentricityOptionSelected = EccentricityFilter.RANGE_07_099

        // Act
        val filter = viewModel.getEccentricityFilter()

        // Assert
        assertEquals(Pair(0.7, 0.99), filter)
    }

    @Test
    fun `inclination any filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)

        // Act
        val filter = viewModel.getInclinationFilter()

        // Assert
        assertEquals(Pair(null, null), filter)
    }

    @Test
    fun `inclination lt_20 filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.inclinationOptionSelected = InclinationFilter.LT_20

        // Act
        val filter = viewModel.getInclinationFilter()

        // Assert
        assertEquals(Pair(null, 20.0), filter)
    }

    @Test
    fun `inclination between_20_60 filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.inclinationOptionSelected = InclinationFilter.BETWEEN_20_60

        // Act
        val filter = viewModel.getInclinationFilter()

        // Assert
        assertEquals(Pair(20.0, 60.0), filter)
    }

    @Test
    fun `inclination between_60_100 filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.inclinationOptionSelected = InclinationFilter.BETWEEN_60_100

        // Act
        val filter = viewModel.getInclinationFilter()

        // Assert
        assertEquals(Pair(60.0, 100.0), filter)
    }

    @Test
    fun `inclination gt_100 filter method returns correct pair`() = runTest {
        // Arrange
        coEvery { mockkUseCase.getApiSatellites(ApiQueryParameters()) } returns satellitesList
        every { NetworkConnectivityProvider.isConnected } returns MutableStateFlow(true)
        viewModel = SatelliteViewModel(mockkUseCase)
        viewModel.inclinationOptionSelected = InclinationFilter.GT_100

        // Act
        val filter = viewModel.getInclinationFilter()

        // Assert
        assertEquals(Pair(100.0, null), filter)
    }
}
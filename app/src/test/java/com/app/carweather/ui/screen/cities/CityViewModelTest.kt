package com.app.carweather.ui.screen.cities

import app.cash.turbine.test
import com.app.carweather.utils.UiState
import com.app.domain.ResponseState
import com.app.domain.usecase.GetCityUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class CityViewModelTest {
    @Mock
    lateinit var getCityUseCase: GetCityUseCase

    private lateinit var viewModel: CityViewModel

    private val testDispatchers = StandardTestDispatcher()

    private var viewData = CityViewModel.ViewData()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatchers)
        viewModel = CityViewModel(getCityUseCase)
    }

    @Test
    fun testViewModelWithDefaultData() {
        Assert.assertEquals(viewData.state, viewModel.viewData.value.state)
        Assert.assertEquals(viewData.errorMessage, viewModel.viewData.value.errorMessage)
        Assert.assertEquals(viewData.cities, viewModel.viewData.value.cities)
    }

    @Test
    fun `when city success, then populate viewData with cities`() {
        runTest {
            val cities = getFakeCities()
            Mockito.`when`(getCityUseCase.invoke()).thenReturn(
                flowOf(
                    ResponseState.Loading(),
                    ResponseState.Success(cities)
                )
            )

            viewModel.getCities()

            viewModel.viewData.test {
                var emittedItem = awaitItem()
                Assert.assertEquals(UiState.LOADING, emittedItem.state)

                emittedItem = awaitItem()
                Assert.assertEquals(UiState.LOADED, emittedItem.state)
                Assert.assertEquals(cities, emittedItem.cities)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when getCities Failure, then populate viewData with error state`() {
        runTest {
            val fakeFailureMsg = "Fake Failure"
            Mockito.`when`(getCityUseCase.invoke()).thenReturn(
                flowOf(
                    ResponseState.Loading(),
                    ResponseState.Error(Throwable(fakeFailureMsg))
                )
            )

            viewModel.getCities()

            viewModel.viewData.test {
                var emittedItem = awaitItem()
                Assert.assertEquals(UiState.LOADING, emittedItem.state)

                emittedItem = awaitItem()
                Assert.assertEquals(UiState.ERROR, emittedItem.state)

                Assert.assertEquals(fakeFailureMsg, emittedItem.errorMessage)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    private fun getFakeCities() = listOf(
        "Los Angeles",
        "Gothenburg",
        "Stockholm",
        "Mountain View",
        "London",
        "New York",
        "Berlin"
    )

    @OptIn(DelicateCoroutinesApi::class)
    @After
    fun close() {
        Dispatchers.shutdown()
    }
}

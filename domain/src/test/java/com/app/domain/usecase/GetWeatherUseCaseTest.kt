package com.app.domain.usecase

import app.cash.turbine.test
import com.app.domain.ResponseState
import com.app.domain.entities.WeatherInfo
import com.app.domain.repositories.IWeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetWeatherUseCaseTest {
    @Mock
    private lateinit var repository: IWeatherRepository

    private lateinit var useCaseTest: GetWeatherUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCaseTest = GetWeatherUseCase(repository)
    }

    @Test
    fun getWeatherInfoSuccessFlow() {
        runTest {
            val cityName = "xyz"
            val unit = "abc"

            Mockito.`when`(repository.getWeatherData(cityName, unit)).then {
                getFakeWeatherInfo()
            }
            val flow = useCaseTest.invoke(cityName, unit)
            flow.test {
                var emittedItem = awaitItem()
                Assert.assertEquals(true, emittedItem is ResponseState.Loading)
                emittedItem = awaitItem()
                Assert.assertEquals(true, emittedItem is ResponseState.Success)
                emittedItem = emittedItem as ResponseState.Success
                Assert.assertEquals(getFakeWeatherInfo(), emittedItem.data)

                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(repository, Mockito.times(1))
                .getWeatherData(cityName, unit)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeatherInfoFailureFlow() {
        runTest {
            val cityName = "xyz"
            val unit = "abc"
            val fakeThrowable = Exception("Something went wrong")
            Mockito.`when`(repository.getWeatherData(cityName, unit)).thenAnswer {
                throw fakeThrowable
            }

            val flow = useCaseTest.invoke(cityName, unit)

            flow.test {
                var emittedItem = awaitItem()
                Assert.assertEquals(true, emittedItem is ResponseState.Loading)

                emittedItem = awaitItem()
                Assert.assertEquals(true, emittedItem is ResponseState.Error)
                emittedItem = emittedItem as ResponseState.Error
                Assert.assertEquals(fakeThrowable.message, emittedItem.throwable.message)

                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(repository, Mockito.times(1))
                .getWeatherData(cityName, unit)
        }
    }

    private fun getFakeWeatherInfo() = WeatherInfo()
}
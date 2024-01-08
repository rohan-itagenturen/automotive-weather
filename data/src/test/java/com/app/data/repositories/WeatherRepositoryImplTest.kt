package com.app.data.repositories

import com.app.data.network.WeatherApi
import com.app.models.data.WeatherData
import com.app.models.domain.WeatherInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    @Mock
    private lateinit var weatherApi: WeatherApi

    private lateinit var weatherRepository: IWeatherRepository

    private val apiKey = "abcd1234"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        weatherRepository = WeatherRepositoryImpl(weatherApi, apiKey)
    }

    @Test
    fun getWeatherData() {
        runTest {
            val cityName = "xyz"
            val unit = "abc"
            val fakeWeatherData = getFakeWeatherData()
            val fakeWeatherInfo = getFakeWeatherInfo()
            Mockito.`when`(weatherApi.getWeatherData(cityName, apiKey, unit)).then { fakeWeatherData }

            val result = weatherRepository.getWeatherData(cityName, unit)
            Assert.assertEquals(result, fakeWeatherInfo)

        }
    }

    private fun getFakeWeatherInfo() = WeatherInfo()
    private fun getFakeWeatherData() = WeatherData(
        base = null,
        clouds = null,
        cod = null,
        coord = null,
        dt = null,
        id = null,
        main = null,
        name = null,
        rain = null,
        sys = null,
        timezone = null,
        visibility = null,
        weather = null,
        wind = null
    )

}
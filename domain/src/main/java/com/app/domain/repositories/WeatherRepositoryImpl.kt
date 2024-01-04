package com.app.domain.repositories

import com.app.data.network.WeatherApi
import com.app.domain.entities.WeatherInfo
import com.app.domain.entities.toWeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val openWeatherKey: String
) : IWeatherRepository {
    override suspend fun getWeatherData(
        cityName: String,
        unit: String
    ): WeatherInfo {
        return weatherApi.getWeatherData(cityName, openWeatherKey, unit).toWeatherInfo()
    }
}
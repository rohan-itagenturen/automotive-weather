package com.app.data.repositories

import com.app.models.domain.WeatherInfo


interface IWeatherRepository {
    suspend fun getWeatherData(cityName: String, unit: String): WeatherInfo
}
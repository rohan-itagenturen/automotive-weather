package com.app.carappservice.di

import com.app.domain.usecase.GetCityUseCase
import com.app.domain.usecase.GetWeatherUseCase
import javax.inject.Inject

class AppComponent @Inject constructor(
    val getCityUseCase: GetCityUseCase,
    val weatherUseCase: GetWeatherUseCase
)
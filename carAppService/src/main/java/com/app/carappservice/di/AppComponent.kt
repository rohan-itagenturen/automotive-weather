package com.app.carappservice.di

import com.app.domain.usecase.CityUseCase
import com.app.domain.usecase.GetWeatherUseCase
import javax.inject.Inject

class AppComponent @Inject constructor(
    val cityUseCase: CityUseCase,
    val weatherUseCase: GetWeatherUseCase
)
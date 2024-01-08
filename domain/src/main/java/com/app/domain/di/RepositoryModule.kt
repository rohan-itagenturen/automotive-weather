package com.app.domain.di

import com.app.data.di.NetworkModule.OPEN_WEATHER_KEY
import com.app.data.network.WeatherApi
import com.app.data.repositories.CityRepositoryImpl
import com.app.data.repositories.ICityRepository
import com.app.data.repositories.IWeatherRepository
import com.app.data.repositories.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherApi: WeatherApi, @Named(OPEN_WEATHER_KEY) openWeatherApiKey: String): IWeatherRepository {
        return WeatherRepositoryImpl(weatherApi, openWeatherApiKey)
    }

    @Provides
    @Singleton
    fun provideCityRepository(): ICityRepository {
        return CityRepositoryImpl()
    }
}
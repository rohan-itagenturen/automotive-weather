package com.app.carappservice.session

import android.content.Intent
import androidx.car.app.Screen
import androidx.car.app.Session
import com.app.carappservice.di.AppComponent
import com.app.carappservice.screens.CityListScreen
import javax.inject.Inject

class CarWeatherSession @Inject constructor(private val appComponent: AppComponent) : Session() {

    override fun onCreateScreen(intent: Intent): Screen {
        return CityListScreen(carContext, appComponent)
    }
}
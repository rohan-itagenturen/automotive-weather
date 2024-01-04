package com.app.carweather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.carweather.ui.screen.cities.SelectCityScreen
import com.app.carweather.ui.screen.weather.ShowWeatherScreen
import com.app.carweather.ui.screen.weather.ShowWeatherViewModel
import com.app.carweather.ui.theme.CarWeatherTheme
import com.app.domain.repositories.ICityRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var cityRepository: ICityRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarWeatherTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.SELECT_CITY) {
                    composable(
                        route = Routes.SELECT_CITY
                    ) {
                        SelectCityScreen(cityRepository.getCities(), onCityClick = {
                            navController.navigate(
                                route = Routes.WEATHER_SCREEN.replace("{name}", it)
                            )
                        })
                    }
                    composable(route = Routes.WEATHER_SCREEN) {
                        val name = it.arguments?.getString("name").orEmpty()
                        val viewModel = hiltViewModel<ShowWeatherViewModel>()

                        ShowWeatherScreen(
                            viewModel = viewModel,
                            cityName = name,
                            onBackPress = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
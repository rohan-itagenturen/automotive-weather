package com.app.carappservice.screens

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarIcon
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.app.carappservice.R
import com.app.carappservice.di.AppComponent
import com.app.domain.ResponseState
import com.app.domain.entities.WeatherInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CityDetailScreen(
    carContext: CarContext,
    private val cityName: String,
    private val appComponent: AppComponent
) : Screen(carContext), DefaultLifecycleObserver {

    private var responseState: ResponseState<WeatherInfo> = ResponseState.Loading()
    private var job: Job? = null
    private var weatherBitmap: Bitmap? = null

    init {
        lifecycle.addObserver(this)

        job = lifecycleScope.launch {
            appComponent.weatherUseCase(cityName, "metric").collect {
                responseState = it
                if (it is ResponseState.Success) {
                    val url = "https://openweathermap.org/img/wn/" + it.data.icon + "@2x.png"
                    val loader = ImageLoader(carContext)
                    val request = ImageRequest.Builder(carContext)
                        .data(url)
                        .allowHardware(false) // Disable hardware bitmaps.
                        .build()

                    val result = (loader.execute(request) as SuccessResult).drawable
                    weatherBitmap = (result as BitmapDrawable).bitmap
                }
                invalidate()
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        job?.cancel()
    }

    override fun onGetTemplate(): Template {
        return when (responseState) {
            is ResponseState.Error -> getErrorTemplate(cityName)
            is ResponseState.Loading -> getLoadingTemplate(cityName)
            is ResponseState.Success -> getSuccessTemplate(
                cityName,
                (responseState as ResponseState.Success).data
            )
        }
    }

    private fun getSuccessTemplate(title: String, weatherInfo: WeatherInfo): Template {
        val paneBuilder = Pane.Builder()
        weatherBitmap?.let {
            paneBuilder
                .setImage(
                    CarIcon.Builder(
                        IconCompat.createWithBitmap(it)
                    ).build()
                )
        }

        paneBuilder
            .addRow(
                Row.Builder()
                    .setImage(
                        CarIcon.Builder(
                            weatherBitmap?.let {
                                IconCompat.createWithBitmap(it)
                            } ?: run {
                                IconCompat.createWithResource(carContext, R.drawable.ic_humidity)
                            }
                        ).build(),
                        Row.IMAGE_TYPE_ICON
                    )
                    .setTitle(
                        carContext.getString(
                            R.string.temperature,
                            weatherInfo.main,
                            weatherInfo.temp.roundToInt().toString()
                        )
                    )
                    .addText(
                        carContext.getString(
                            R.string.high_low_temp,
                            weatherInfo.temp_max.roundToInt().toString(),
                            weatherInfo.temp_min.roundToInt().toString()
                        )
                    )
                    .build()
            )
            .addRow(
                Row.Builder()
                    .setImage(
                        CarIcon.Builder(
                            IconCompat.createWithResource(carContext, R.drawable.ic_humidity)
                        ).build(),
                        Row.IMAGE_TYPE_ICON
                    )
                    .setTitle(
                        carContext.getString(
                            R.string.humidity,
                            weatherInfo.humidity.toString()
                        )
                    )
                    .build()
            )
            .addRow(
                Row.Builder()
                    .setImage(
                        CarIcon.Builder(
                            IconCompat.createWithResource(carContext, R.drawable.ic_wind)
                        ).build(),
                        Row.IMAGE_TYPE_ICON
                    )
                    .setTitle(carContext.getString(R.string.wind, weatherInfo.speed.toString()))
                    .build()
            )
            .addRow(
                Row.Builder()
                    .setImage(
                        CarIcon.Builder(
                            IconCompat.createWithResource(carContext, R.drawable.ic_sunrise)
                        ).build(),
                        Row.IMAGE_TYPE_ICON
                    )
                    .setTitle(carContext.getString(R.string.sunrise, weatherInfo.sunrise))
                    .build()
            )
            .addRow(
                Row.Builder()
                    .setImage(
                        CarIcon.Builder(
                            IconCompat.createWithResource(carContext, R.drawable.ic_sunset)
                        ).build(),
                        Row.IMAGE_TYPE_ICON
                    )
                    .setTitle(carContext.getString(R.string.sunset, weatherInfo.sunset))
                    .build()
            )
            .addRow(
                Row.Builder()
                    .setImage(
                        CarIcon.Builder(
                            IconCompat.createWithResource(carContext, R.drawable.ic_visibility)
                        ).build(),
                        Row.IMAGE_TYPE_ICON
                    )
                    .setTitle(
                        carContext.getString(
                            R.string.visibility,
                            weatherInfo.visibility.toString()
                        )
                    )
                    .build()
            )
            .addRow(
                Row.Builder()
                    .setImage(
                        CarIcon.Builder(
                            IconCompat.createWithResource(carContext, R.drawable.ic_pressure)
                        ).build(),
                        Row.IMAGE_TYPE_ICON
                    )
                    .setTitle(
                        carContext.getString(
                            R.string.pressure,
                            weatherInfo.pressure.toString()
                        )
                    )
                    .build()
            )


        val builder = PaneTemplate.Builder(
            paneBuilder.build()
        )
            .setTitle(title)
            .setHeaderAction(Action.BACK)

        return builder.build()
    }


    private fun getErrorTemplate(title: String): Template {
        return MessageTemplate.Builder(
            carContext.getString(R.string.something_went_wrong)
        )
            .setTitle(title)
            .addAction(
                Action.Builder()
                    .setTitle(carContext.getString(R.string.go_back))
                    .setOnClickListener { screenManager.pop() }
                    .build()
            )
            .setHeaderAction(Action.APP_ICON)
            .build()
    }

    private fun getLoadingTemplate(title: String): Template {
        val builder = PaneTemplate.Builder(
            Pane.Builder()
                .setLoading(true)
                .build()
        )
            .setTitle(title)
            .setHeaderAction(Action.BACK)

        return builder.build()
    }
}
package com.app.carappservice.screens

import androidx.annotation.DrawableRes
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarIcon
import androidx.car.app.model.GridItem
import androidx.car.app.model.GridTemplate
import androidx.car.app.model.Item
import androidx.car.app.model.ItemList
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.DefaultLifecycleObserver
import com.app.carappservice.R
import com.app.models.domain.WeatherInfo

class WeatherDetailScreen(
    carContext: CarContext,
    private val weatherInfo: WeatherInfo
) : Screen(carContext), DefaultLifecycleObserver {
    override fun onGetTemplate(): Template {
        val gridItemListBuilder = ItemList.Builder()
        gridItemListBuilder
            .addItem(
                createGridItem(
                    R.drawable.ic_humidity,
                    carContext.getString(R.string.humidity),
                    carContext.getString(R.string.humidity_value, weatherInfo.humidity.toString())
                )
            )
            .addItem(
                createGridItem(
                    R.drawable.ic_wind,
                    carContext.getString(R.string.wind),
                    carContext.getString(R.string.wind_value, weatherInfo.speed.toString())
                )
            )
            .addItem(
                createGridItem(
                    R.drawable.ic_sunrise,
                    carContext.getString(R.string.sunrise),
                    carContext.getString(R.string.sunrise_value, weatherInfo.sunrise)
                )
            )
            .addItem(
                createGridItem(
                    R.drawable.ic_sunset,
                    carContext.getString(R.string.sunset),
                    carContext.getString(R.string.sunset_value, weatherInfo.sunset)
                )
            )
            .addItem(
                createGridItem(
                    R.drawable.ic_visibility,
                    carContext.getString(R.string.visibility),
                    carContext.getString(R.string.visibility_value, weatherInfo.visibility.toString())
                )
            )
            .addItem(
                createGridItem(
                    R.drawable.ic_pressure,
                    carContext.getString(R.string.pressure),
                    carContext.getString(R.string.pressure_value, weatherInfo.pressure.toString())
                )
            )

        val builder = GridTemplate.Builder()
            .setSingleList(gridItemListBuilder.build())
            .setTitle(weatherInfo.name)
            .setHeaderAction(Action.BACK)

        return builder.build()
    }

    private fun createGridItem(@DrawableRes drawableRes: Int, title: String, text: String): Item {
        return GridItem.Builder()
            .setImage(
                CarIcon.Builder(
                    IconCompat.createWithResource(carContext, drawableRes)
                ).build(),
                GridItem.IMAGE_TYPE_ICON
            )
            .setTitle(title)
            .setText(text)
            .build()
    }
}


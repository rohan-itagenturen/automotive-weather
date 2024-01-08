package com.app.carappservice.screens

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.app.carappservice.R
import com.app.carappservice.di.AppComponent
import com.app.domain.ResponseState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CityListScreen(
    carContext: CarContext,
    private val appComponent: AppComponent
) : Screen(carContext), DefaultLifecycleObserver {

    private var responseState: ResponseState<List<String>> = ResponseState.Loading()

    private var job: Job? = null

    init {
        lifecycle.addObserver(this)

        job = lifecycleScope.launch {
            appComponent.getCityUseCase().collect {
                responseState = it
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
            is ResponseState.Error -> getErrorTemplate()
            is ResponseState.Loading -> getLoadingTemplate()
            is ResponseState.Success -> getSuccessTemplate((responseState as ResponseState.Success).data)
        }
    }

    private fun getSuccessTemplate(cities: List<String>): Template {
        val itemListBuilder = ItemList.Builder()
        cities.forEach {
            itemListBuilder.addItem(
                Row.Builder()
                    .setTitle(it)
                    .setBrowsable(true)
                    .setOnClickListener {
                        screenManager.push(WeatherInfoScreen(carContext, it, appComponent))
                    }
                    .build()
            )
        }

        val builder = ListTemplate.Builder()
            .setSingleList(itemListBuilder.build())
            .setTitle(carContext.getString(R.string.weather_app))
            .setHeaderAction(Action.APP_ICON)

        return builder.build()
    }


    private fun getErrorTemplate(): Template {
        return MessageTemplate.Builder(
            carContext.getString(R.string.something_went_wrong)
        )
            .setTitle(carContext.getString(R.string.weather_app))
            .addAction(
                Action.Builder()
                    .setTitle(carContext.getString(R.string.go_back))
                    .setOnClickListener { screenManager.pop() }
                    .build()
            )
            .setHeaderAction(Action.APP_ICON)
            .build()
    }

    private fun getLoadingTemplate(): Template {
        val builder = ListTemplate.Builder()
            .setLoading(true)
            .setTitle(carContext.getString(R.string.weather_app))
            .setHeaderAction(Action.APP_ICON)

        return builder.build()
    }
}

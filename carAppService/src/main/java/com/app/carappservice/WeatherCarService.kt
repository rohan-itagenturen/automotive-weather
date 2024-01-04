package com.app.carappservice

import android.content.pm.ApplicationInfo
import androidx.car.app.CarAppService
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator
import com.app.carappservice.session.CarWeatherSession
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WeatherCarService : CarAppService() {

    @Inject
    lateinit var carWeatherSession : CarWeatherSession

    override fun createHostValidator(): HostValidator {
        // For debugging purpose allow all hosts.
        return if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
        } else {
            // For production purpose allow default hosts that are available in car app libs.
            HostValidator.Builder(baseContext)
                .addAllowedHosts(R.array.hosts_allowlist)
                .build()
        }
    }

    override fun onCreateSession(): Session {
        return carWeatherSession
    }
}
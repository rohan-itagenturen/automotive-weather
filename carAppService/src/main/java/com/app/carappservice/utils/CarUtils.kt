package com.app.carappservice.utils

import android.content.Intent
import android.net.Uri
import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.HostException
import com.app.carappservice.R

class CarUtils {
    companion object {
        fun navigateTo(carContext: CarContext, placeName: String) {
            val uri = Uri.parse("geo:0,0?q=$placeName")
            val intent = Intent(CarContext.ACTION_NAVIGATE, uri)

            try {
                carContext.startCarApp(intent)
            } catch (e: HostException) {
                CarToast.makeText(
                    carContext,
                    carContext.getString(R.string.fail_start_nav),
                    CarToast.LENGTH_LONG
                ).show()
            }
        }
    }
}
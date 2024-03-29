package com.app.carweather.ui.screen.cities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.carweather.R
import com.app.carweather.utils.UiState
import com.app.weatherapp.widgets.BaseScaffold

@Composable
fun SelectCityScreen(viewModel: CityViewModel, onCityClick: (String) -> Unit) {

    val viewData = viewModel.viewData.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getCities()
    }

    BaseScaffold(
        title = stringResource(R.string.select_city),
        showBackButton = false
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (viewData.value.state) {
                UiState.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("viewLoader")
                    )
                }

                UiState.LOADED -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(
                            items = viewData.value.cities,
                        ) { cityName ->
                            Card(
                                elevation = CardDefaults.cardElevation(4.dp),
                                shape = RoundedCornerShape(
                                    topEnd = 10.dp,
                                    topStart = 10.dp,
                                    bottomEnd = 10.dp,
                                    bottomStart = 10.dp
                                ),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 10.dp)
                                    .clickable { onCityClick(cityName) }

                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(all = 10.dp),
                                    text = cityName,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        }
                    }
                }

                UiState.ERROR -> {
                    Text(
                        text = stringResource(R.string.something_went_wrong),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("errorView")
                    )
                }
            }
        }
    }
}

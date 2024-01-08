package com.app.carweather.ui.screen.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.carweather.utils.UiState
import com.app.domain.ResponseState
import com.app.domain.usecase.GetCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val getCitiesUseCase: GetCityUseCase
) : ViewModel() {

    private val _viewData = MutableStateFlow(ViewData())
    val viewData: StateFlow<ViewData> get() = _viewData.asStateFlow()

    fun getCities() {
        viewModelScope.launch {
            getCitiesUseCase()
                .collect { response ->
                    when (response) {
                        is ResponseState.Error -> {
                            _viewData.value = _viewData.value.copy(
                                state = UiState.ERROR,
                                errorMessage = response.throwable.message.orEmpty()
                            )
                        }

                        is ResponseState.Loading -> {
                            _viewData.value = _viewData.value.copy(
                                state = UiState.LOADING
                            )
                        }

                        is ResponseState.Success -> {
                            _viewData.value = _viewData.value.copy(
                                state = UiState.LOADED,
                                cities = response.data
                            )
                        }
                    }
                }

        }
    }

    data class ViewData(
        val state: UiState = UiState.LOADING,
        val cities: List<String> = listOf(),
        val errorMessage: String = ""
    )
}
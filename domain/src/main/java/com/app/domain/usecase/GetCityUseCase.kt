package com.app.domain.usecase

import com.app.data.repositories.ICityRepository
import com.app.domain.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCityUseCase @Inject constructor(private val cityRepository: ICityRepository) {
    suspend operator fun invoke(): Flow<ResponseState<List<String>>> {
        return flow {
            try {
                emit(ResponseState.Loading())
                emit(
                    ResponseState.Success(cityRepository.getCities())
                )
            } catch (e: Exception) {
                emit(ResponseState.Error(e))
            }
        }
    }
}
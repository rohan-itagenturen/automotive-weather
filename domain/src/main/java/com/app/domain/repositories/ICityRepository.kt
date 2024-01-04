package com.app.domain.repositories

interface ICityRepository {
    fun getCities() : List<String>
}
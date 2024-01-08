package com.app.data.repositories

interface ICityRepository {
    fun getCities() : List<String>
}
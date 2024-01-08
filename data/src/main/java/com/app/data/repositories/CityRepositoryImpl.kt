package com.app.data.repositories

class CityRepositoryImpl : ICityRepository {
    override fun getCities(): List<String> {
        return listOf(
            "Los Angeles",
            "Gothenburg",
            "Stockholm",
            "Mountain View",
            "London",
            "New York",
            "Berlin"
        )
    }
}
package com.app.domain.repositories

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
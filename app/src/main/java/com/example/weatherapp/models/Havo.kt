package com.example.weatherapp.models

data class Havo(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)
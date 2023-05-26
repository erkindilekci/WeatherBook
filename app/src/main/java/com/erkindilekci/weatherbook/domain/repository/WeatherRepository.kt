package com.erkindilekci.weatherbook.domain.repository

import com.erkindilekci.weatherbook.domain.util.Resource
import com.erkindilekci.weatherbook.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}

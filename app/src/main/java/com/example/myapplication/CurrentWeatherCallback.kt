package com.example.myapplication

interface CurrentWeatherCallback {
    fun onCurrentWeather(currentWeather: CurrentWeather)
    fun onError(exception: Exception?)
}
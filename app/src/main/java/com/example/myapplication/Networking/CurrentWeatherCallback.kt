package com.example.myapplication.Networking

import Model.CurrentWeather

interface CurrentWeatherCallback {
    fun onCurrentWeather(currentWeather: CurrentWeather)
    fun onError(exception: Exception?)
}
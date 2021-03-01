package com.example.myapplication

class CurrentWeather(val location: String,
                     val conditionId: Int,
                     val weatherCondition: String,
                     val tempKelvin: Double) {

    val tempFahrenheit: Int
        get() = (tempKelvin * 9 / 5 - 459.67).toInt()
}
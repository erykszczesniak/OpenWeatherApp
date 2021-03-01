package com.example.myapplication.View


import Model.CurrentWeather
import com.example.myapplication.Utils.CurrentWeatherUtils.getWeatherIconResId
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.os.Bundle
import android.text.TextWatcher
import android.text.Editable
import android.widget.ImageView

import android.view.View
import android.widget.*
import com.example.myapplication.Networking.CurrentWeatherCallback
import com.example.myapplication.Networking.CurrentWeatherService
import com.example.myapplication.R

import java.lang.Exception

class WeatherActivity : AppCompatActivity() {


    private lateinit var currentWeatherService: CurrentWeatherService
    private lateinit var weatherContainer: View
    private lateinit var weatherProgressBar: ProgressBar
    private lateinit var temperature: TextView
    private lateinit var location: TextView
    private lateinit var weatherCondition: TextView
    private lateinit var weatherConditionIcon: ImageView
    private lateinit var locationField: EditText
    private lateinit var fab: FloatingActionButton
    private var fetchingWeather: Boolean = false
    private var textCount = 0
    private var currentLocation = "London"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity)
        currentWeatherService = CurrentWeatherService(this)
        weatherContainer = findViewById(R.id.weather_container)
        weatherProgressBar = findViewById(R.id.weather_progress_bar)
        temperature = findViewById(R.id.temperature)
        location = findViewById(R.id.location)
        weatherCondition = findViewById(R.id.weather_condition)
        weatherConditionIcon = findViewById(R.id.weather_condition_icon)
        locationField = findViewById(R.id.location_field)
        fab = findViewById(R.id.fab)
        locationField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var count = count
                count = s.toString().trim { it <= ' ' }.length
                fab.setImageResource(if (count == 0) R.drawable.ic_refresh else R.drawable.ic_search)
                textCount = count
            }

            override fun afterTextChanged(s: Editable) {}
        })
        fab.setOnClickListener(View.OnClickListener {
            if (textCount == 0) {
                refreshWeather()
            } else {
                searchForWeather(locationField.getText().toString())
                locationField.setText("")
            }
        })

        // Start a search for the default location
        searchForWeather(currentLocation)
    }

    override fun onDestroy() {
        super.onDestroy()
        currentWeatherService!!.cancel()
    }

    private fun refreshWeather() {
        if (fetchingWeather) {
            return
        }
        searchForWeather(currentLocation)
    }

    private fun searchForWeather(location: String) {
        toggleProgress(true)
        fetchingWeather = true
        currentWeatherService.getCurrentWeather(location, currentWeatherCallback)
    }

    private fun toggleProgress(showProgress: Boolean) {
        weatherContainer.visibility = if (showProgress) View.GONE else View.VISIBLE
        weatherProgressBar.visibility = if (showProgress) View.VISIBLE else View.GONE
    }

    private val currentWeatherCallback: CurrentWeatherCallback = object : CurrentWeatherCallback {
        override fun onCurrentWeather(currentWeather: CurrentWeather) {
            currentLocation = currentWeather.location
            temperature.text = currentWeather.tempFahrenheit.toString()
            location.text = currentWeather.location
            weatherCondition!!.text = currentWeather.weatherCondition
            weatherConditionIcon!!.setImageResource(getWeatherIconResId(currentWeather.conditionId))
            toggleProgress(false)
            fetchingWeather = false
        }

        override fun onError(exception: Exception?) {
            toggleProgress(false)
            fetchingWeather = false
            Toast.makeText(this@WeatherActivity, "There was an error fetching weather, " +
                    "try again.", Toast.LENGTH_SHORT).show()
        }
    }
}
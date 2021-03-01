package com.example.myapplication

import android.app.Activity
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



class CurrentWeatherService(activity: Activity) {
    private val queue: RequestQueue

    companion object {
        private val TAG = CurrentWeatherService::class.java.simpleName
        private const val URL = "https://api.openweathermap.org/data/2.5/weather"
        private const val CURRENT_WEATHER_TAG = "CURRENT_WEATHER"
        private const val API_KEY = "YOUR API KEY HERE"
    }

    init {
        queue = Volley.newRequestQueue(activity.applicationContext)
    }

    fun getCurrentWeather(locationName: String, callback: CurrentWeatherCallback) {
        val url = java.lang.String.format("%s?q=%s&appId=%s", URL, locationName, API_KEY)
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    try {
                        val currentWeatherJSONObject = JSONObject(response)
                        val weather = currentWeatherJSONObject.getJSONArray("weather")
                        val weatherCondition = weather.getJSONObject(0)
                        val locationName = currentWeatherJSONObject.getString("name")
                        val conditionId = weatherCondition.getInt("id")
                        val conditionName = weatherCondition.getString("main")
                        val tempKelvin = currentWeatherJSONObject.getJSONObject("main").getDouble("temp")
                        val currentWeather = CurrentWeather(locationName, conditionId, conditionName, tempKelvin)
                        callback.onCurrentWeather(currentWeather)
                    } catch (e: JSONException) {
                        callback.onError(e)
                    }
                }) { error -> callback.onError(error) }
        stringRequest.tag = CURRENT_WEATHER_TAG
        queue.add(stringRequest)
    }

    fun cancel() {
        queue.cancelAll(CURRENT_WEATHER_TAG)
    }
}
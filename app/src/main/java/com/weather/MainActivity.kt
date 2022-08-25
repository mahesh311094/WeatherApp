package com.weather

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.weather.adapter.WeatherAdapter
import com.weather.network.UserService
import com.weather.utils.Const

class MainActivity : BaseActivity() {
    private lateinit var binding: com.weather.databinding.ActivityMainBinding

    private var selectedLocation: String = ""
    private var selectedLatitude: String = ""
    private var selectedLongitude: String = ""

    private var isFrom: String = "Current"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
    }

    private fun init() {

        // current or type weather change event
        binding.rgLocation.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                R.id.rbCurrent -> {
                    isFrom = "Current"
                    if (latitude != 0.0 && longitude != 0.0) {
                        setCurrentLocation()
                    } else {
                        checkLocation()
                    }
                }
                R.id.rbManual -> {
                    isFrom = "Other"
                    binding.etLocation.isEnabled = true
                    binding.etLocation.isFocusable = true
                    binding.etLocation.isFocusableInTouchMode = true
                    binding.etLocation.setText("")
                }
            }
        }

        binding.btnFetchDetails.setOnClickListener {
            selectedLocation = binding.etLocation.text.toString()
            if (selectedLocation.isNotEmpty()) {
                getWeatherDetails()
            } else {
                Toast.makeText(this, "Please enter your location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWeatherDetails() {
        when (isFrom) {
            "Current" -> {
                selectedLatitude = latitude.toString()
                selectedLongitude = longitude.toString()
            }
            "Other" -> {
                // get latitude longitude from input address
                val getLatLong = getLatLongFromAddress(this, selectedLocation)
                selectedLatitude = getLatLong?.latitude.toString()
                selectedLongitude = getLatLong?.longitude.toString()
            }
        }

        // api call for get current weather data
        UserService.fetchDetails(this, selectedLatitude, selectedLongitude, Const.APP_ID) {
            if (it != null) {
                binding.rlWeather.visibility = View.VISIBLE
                binding.tvTemp.text = it.main.temp.toString()
                binding.tvWeather.text = it.weather[0].main
                binding.tvWind.text = getString(R.string.wind, it.wind.speed.toString())
                binding.tvHumidity.text =
                    getString(R.string.humidity, it.main.humidity.toString() + "%")
            } else {
                binding.rlWeather.visibility = View.GONE
                Toast.makeText(this, Const.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }

        // api call for next 7 day forecast weather data
        UserService.fetchForecastDetails(this, selectedLatitude, selectedLongitude, Const.APP_ID) {
            if (it != null) {
                binding.rlWeather.visibility = View.VISIBLE
                binding.recyclerView.adapter = WeatherAdapter(it.list)
            } else {
                binding.tvError.text = getString(R.string.error)
            }
        }
    }

    // when current location detected this function will be called
    override fun onLocationDetect() {
        super.onLocationDetect()
        if (latitude != 0.0 && longitude != 0.0 && isFrom == "Current") {
            setCurrentLocation()
        }
    }

    // set current location to view
    private fun setCurrentLocation() {
        binding.etLocation.isEnabled = false
        binding.etLocation.isFocusable = false
        binding.etLocation.isFocusableInTouchMode = false
        binding.etLocation.setText(address)
    }
}
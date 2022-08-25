package com.weather

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.weather.utils.Utils
import java.io.IOException
import java.util.*

open class BaseActivity : AppCompatActivity() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var address: String = ""
    private val locationPermissionReqCode = 1000

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val reqSetting = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        smallestDisplacement = 1.0f
    }

    // check location permission
    fun checkLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkLocationPermission(this)) {
            getLocation()
        }
    }

    // location service permission result
    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                Log.e("LOG", "Back from REQUEST_CHECK_STATE")
                getLocation()
            } else {
                Toast.makeText(this, "Need Location Permission", Toast.LENGTH_LONG).show()
            }
        }

    //getting current location update
    private val locationUpdates = object : LocationCallback() {
        override fun onLocationResult(lr: LocationResult) {
            try {
                Utils.dismissDialog()
                latitude = lr.lastLocation!!.latitude
                longitude = lr.lastLocation!!.longitude
                address = getAddressFromLatLong(applicationContext, latitude, longitude)
                onLocationDetect()
                Log.e(
                    "=== Location ===",
                    "latitude: $latitude longitude: $longitude address: $address"
                )
                stopLocationUpdate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // check location permission ACCESS_FINE_LOCATION
    private fun checkLocationPermission(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionReqCode
            )
            return false
        }
        return true
    }

    // location permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            locationPermissionReqCode -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                } else {
                    Toast.makeText(this, "You need to grant permission", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    // get current location and request to location service
    private fun getLocation() {
        Utils.showProgressDialog(this)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(reqSetting)
        val client = LocationServices.getSettingsClient(this)

        client.checkLocationSettings(builder.build()).addOnSuccessListener {
            //noinspection MissingPermission
            fusedLocationClient.requestLocationUpdates(
                reqSetting,
                locationUpdates,
                Looper.getMainLooper()
            )
        }.addOnFailureListener {
            Utils.dismissDialog()
            when ((it as ApiException).statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val rae = it as ResolvableApiException
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(rae.resolution).build()
                        resolutionForResult.launch(intentSenderRequest)
                    } catch (sie: IntentSender.SendIntentException) {
                        Log.e("===== Error1 =====", "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    val errorMessage =
                        "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    Log.e("====== Error2 =====", errorMessage)
                }
            }
        }
    }

    // call this function when current location detected
    open fun onLocationDetect() {}

    // stop location update
    private fun stopLocationUpdate() {
        fusedLocationClient.removeLocationUpdates(locationUpdates)
    }

    //get Address From Latitude Longitude
    fun getAddressFromLatLong(context: Context, latitude: Double, longitude: Double): String {
        val addresses: List<Address>
        val geoCoder = Geocoder(context, Locale.getDefault())
        addresses = geoCoder.getFromLocation(
            latitude,
            longitude,
            1
        )
        return addresses[0].getAddressLine(0)
    }

    //get Latitude Longitude From Address
    fun getLatLongFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            p1 = LatLng(location.latitude, location.longitude)

            Log.e("========lat long", "${location.latitude} ${location.longitude}")
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }
}
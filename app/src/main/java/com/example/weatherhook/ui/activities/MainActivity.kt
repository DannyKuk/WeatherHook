package com.example.weatherhook.ui.activities

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherhook.data.api.Api
import com.example.weatherhook.data.db.SQLiteHelper
import com.example.weatherhook.data.models.ForecastData
import com.example.weatherhook.data.models.ForecastDay
import com.example.weatherhook.data.repository.ForecastRepo
import com.example.weatherhook.databinding.ActivityMainBinding
import com.example.weatherhook.services.locationService.LocationService
import com.example.weatherhook.services.notificationService.*
import com.example.weatherhook.services.notificationService.Notification
import com.google.android.gms.location.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val PERMISSIONS_REQUEST_CODE = 123

    val forecastRepo = ForecastRepo()
    val db = SQLiteHelper(this)


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (checkPermissions()) {

                recreate()
            } else {
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val noticationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
        return locationPermission == PackageManager.PERMISSION_GRANTED && noticationPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the shared preferences file
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        // Get the editor for the shared preferences file
        val editor = sharedPreferences.edit()


        // Set a boolean value in the shared preferences file
        editor.putBoolean("key", true)


        // Save the changes to the shared preferences file
        editor.apply()


        if (!checkPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                PERMISSIONS_REQUEST_CODE
            )
        }





        //setContentView(R.layout.activity_main)
        val action=supportActionBar
        action!!.title = "Weather Hook Home"

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) { createNotificationChannel() }

        val location = LocationService().getLocationPair(this)
        val locationName = LocationService().getLocationName(this, location.first.toDouble(), location.second.toDouble()) ?: "Berlin"
        val apiLocationName = forecastRepo.getName(db)

        val latitude = location.first
        val longitude = location.second

        Log.e("shit", "new Name $locationName")
        Log.e("shit", "new Name $location")


        if (forecastRepo.getForecast(db) == ForecastData(listOf<ForecastDay>().toMutableList())){
            Log.e("shit", "First start fetch")
            Api().callApi(location.first,location.second,7, this) { forecast ->
                if (forecast.cod == "200") {
                    forecastRepo.addForecast(forecast, locationName, this, SQLiteHelper(this))
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    setContentView(binding.root)
                    Notification(this).scheduleNotification(forecast.city.name, "It is ${(forecast.list[0].temp.max).toInt()-273.15} °C")
                } else {
                    val test = forecast.city.name
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    setContentView(binding.root)
                    Log.e("shit", test.toString())
                }
            }

        }else{
            if(locationName == apiLocationName) {
                Log.e("shit", "data from database")
                Log.e("shit", "Location Name: $locationName")
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
            }
            else {
                Log.e("shit", "data from fetch")
                Api().callApi(location.first, location.second,7, this) { forecast ->
                    if (forecast.cod == "200") {
                        forecastRepo.updateForecast(forecast, locationName,this, SQLiteHelper(this))
                        binding = ActivityMainBinding.inflate(layoutInflater)
                        setContentView(binding.root)
                        Notification(this).scheduleNotification(forecast.city.name, "It is ${(forecast.list[0].deg).toInt()-273.15} °C")
                    } else {
                        val test = forecast.city.name
                        binding = ActivityMainBinding.inflate(layoutInflater)
                        setContentView(binding.root)
                        Log.e("shit", test.toString())
                    }
                }
            }

        }


    }


    fun openMap(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)

    }

    fun newHook(view: View) {
        val intent = Intent(this, HookActivity::class.java)
        intent.putExtra("currentEvent", -2)
        startActivity(intent)

    }

    fun openSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "AlertNotifier"
        val desc = "Send Alert Notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}

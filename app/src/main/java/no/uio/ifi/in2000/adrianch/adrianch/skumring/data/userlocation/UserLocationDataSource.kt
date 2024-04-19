package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class UserLocation (context: Context) {
    private val logTag = "UserLocation"
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private var long = "10.718393"
    private var lat = "59.943735"

    // TODO check for permissions lol
    @SuppressLint("MissingPermission")
    private fun getLastLocation(fusedLocationClient: FusedLocationProviderClient, context: Context) {
        Log.d(logTag, "Trying to fetch loc")

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                long = location.longitude.toString()
                lat = location.latitude.toString()
                Log.d(logTag, "Long: $long Lat: $lat")
            }
        }
    }
}
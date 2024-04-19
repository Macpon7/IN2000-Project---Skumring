package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.userlocation.UserLocation

class UserLocationDataSource (context: Context) {
    private val logTag = "UserLocationDataSource"
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private var long = "10.718393"
    private var lat = "59.943735"

    // TODO check for permissions lol
    @SuppressLint("MissingPermission")
    private fun getLastLocation(fusedLocationClient: FusedLocationProviderClient): UserLocation {
        Log.d(logTag, "Trying to fetch loc")

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                long = location.longitude.toString()
                lat = location.latitude.toString()
                Log.d(logTag, "Long: $long Lat: $lat")
            }
        }

        return UserLocation(long = long, lat = lat)
    }
}
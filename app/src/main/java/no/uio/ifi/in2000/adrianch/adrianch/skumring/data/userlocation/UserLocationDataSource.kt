package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.userlocation.UserLocation

class UserLocationDataSource (
    private val application: Application
) {
    private val logTag = "UserLocationDataSource"
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    // Default loc is coordinates over OJD
    private var long = "10.718393"
    private var lat = "59.943735"

    suspend fun getUserLocation(application: Application): UserLocation {
        val userLocation = getLastLocation()
        return if (userLocation == null) {
            UserLocation(long = long, lat = lat)
        } else {
            long = userLocation.longitude.toString()
            lat = userLocation.latitude.toString()
            UserLocation(long = long, lat = lat)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getLastLocation(): Location? {
        Log.d(logTag, "Trying to fetch loc")

        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = application.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled && !(hasAccessCoarseLocationPermission || hasAccessFineLocationPermission)) {
            return null
        }

        return suspendCancellableCoroutine { cont ->
            fusedLocationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result) {} // Resume coroutine with location result
                    } else {
                        cont.resume(null) {} // Resume coroutine with null location result
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it) {}  // Resume coroutine with location result
                }
                addOnFailureListener {
                    cont.resume(null) {} // Resume coroutine with null location result
                }
                addOnCanceledListener {
                    cont.cancel() // Cancel the coroutine
                }
            }
        }
    }
}
package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.userlocation.UserLocation

/**
 * Main object that keeps track of device location when asked
 */
class UserLocationDataSource(private val context: Context) {

    //Constant for logcatting
    private val logTag = "UserLocationDataSource"

    // Initializing location client
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Default loc is coordinates over OJD to be returned if we fail to access location
    private var long = "0"
    private var lat = "0"
    private var bearing = 0.0f

    // Available function that returns user location object we designed
    // If it fails to fetch location, the users location defaults to OJD
    suspend fun getUserLocation(): UserLocation {
        val userLocation = getLastLocation()
        return if (userLocation == null) {
            Log.d(logTag, "Failed to get loc")
            UserLocation(long = long, lat = lat, bearing = bearing)
        } else {
            long = userLocation.longitude.toString()
            lat = userLocation.latitude.toString()
            bearing = userLocation.bearing
            Log.d(logTag, "Lat = $lat, long = $long")
            UserLocation(long = long, lat = lat, bearing = bearing)
        }
    }

    // Main function that handles the location query
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getLastLocation(): Location? {

        // Gathers data on whether or not we have necessary permissions
        val isFineEnabled = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val isCourseEnabled = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )

        // If we don't have permissions, return null object
        return if (!isGpsEnabled && !(isCourseEnabled || isFineEnabled)) {
            null
        } else {
            // Else, return updated or last recorded location of user
            suspendCancellableCoroutine { cont ->
                fusedLocationClient.lastLocation.apply {
                    if (isComplete) {
                        if (isSuccessful) {
                            cont.resume(result) {} // If successful, resume coroutine returning result
                        } else {
                            cont.resume(null) {} // If unsuccessful, resume coroutine returning null
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
}
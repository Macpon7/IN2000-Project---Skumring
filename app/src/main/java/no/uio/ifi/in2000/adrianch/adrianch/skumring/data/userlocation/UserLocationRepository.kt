package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation

import android.app.Application
import android.content.Context
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.userlocation.UserLocation

private const val logTag = "UserLocationRepo"


interface UserLocationRepository {

    /**
 * Calls the [UserLocationDataSource] to return a [UserLocation] object containing
 * info about device location.
 */
    suspend fun getUserLocation(): UserLocation
}

class UserLocationRepositoryImpl (context: Context): UserLocationRepository {

    private val userLocationDataSource: UserLocationDataSource = UserLocationDataSource(
        context = context)

    override suspend fun getUserLocation(): UserLocation {
        return userLocationDataSource.getUserLocation()
    }
}
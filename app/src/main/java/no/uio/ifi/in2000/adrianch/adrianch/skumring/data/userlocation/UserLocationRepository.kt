package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation

import android.app.Application
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.userlocation.UserLocation

private const val logTag = "UserLocationRepo"

interface UserLocationRepository {
    suspend fun getUserLocation(): UserLocation
}
class UserLocationRepositoryImpl (application: Application) {

    private val userLocationDataSource: UserLocationDataSource = UserLocationDataSource(
        application = application)

    override suspend fun getUserLocation(): UserLocation {
        return userLocationDataSource.getUserLocation()
    }
}
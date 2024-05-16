package no.uio.ifi.in2000.adrianch.adrianch.skumring.directions

import com.google.gson.Gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.directions.DirectionsDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.Directions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.MeansOfTransportation
import org.junit.Test

class DirectionsDataSourceTest {
    private val gson = Gson()
    private val source = DirectionsDataSource()
    private val walking = MeansOfTransportation.WALKING
    private val biking = MeansOfTransportation.BIKING
    private val driving = MeansOfTransportation.DRIVING

    private val walkingUioToOsloMetTestData: Directions = gson.fromJson(
        walkingUioToOsloMet, Directions::class.java
    )
    private val cyclingUioToOsloMetTestData: Directions = gson.fromJson(
        cyclingUioToOsloMet, Directions::class.java
    )
    private val drivingUioToOsloMetTestData: Directions = gson.fromJson(
        drivingUioToOsloMet, Directions::class.java
    )
    private val drivingNullIslandToOsloMetTestData: Directions = gson.fromJson(
        drivingNullIslandToOsloMet, Directions::class.java
    )

    /*
    Testing that the responses get properly converted from json as we can see with
    our own eyes to the object containing the data we want.
     */
    @Test
    fun checkWalkingDurationDistance() {
        val expectedHours = "0"
        val expectedMinutes = "37"
        val expectedDistance = "3.3"
        val responseMinutes = source.convertResponseToTravelDurationDistance(
            walkingUioToOsloMetTestData, walking
        ).durationMinutes
        val responseHours = source.convertResponseToTravelDurationDistance(
            walkingUioToOsloMetTestData, walking
        ).durationHours
        val responseDistance = source.convertResponseToTravelDurationDistance(
            walkingUioToOsloMetTestData, walking
        ).distance
        assert(
            (expectedDistance == responseDistance && expectedMinutes == responseMinutes && expectedHours == responseHours)
        )

    }

    @Test
    fun checkCyclingDurationDistance() {
        val expectedHours = "0"
        val expectedMinutes = "15"
        val expectedDistance = "3.5"
        val responseMinutes = source.convertResponseToTravelDurationDistance(
            cyclingUioToOsloMetTestData, biking
        ).durationMinutes
        val responseHours = source.convertResponseToTravelDurationDistance(
            cyclingUioToOsloMetTestData, biking
        ).durationHours
        val responseDistance = source.convertResponseToTravelDurationDistance(
            cyclingUioToOsloMetTestData, biking
        ).distance

        assert(
            (expectedDistance == responseDistance && expectedMinutes == responseMinutes && expectedHours == responseHours)
        )

    }

    @Test
    fun checkDrivingDurationDistance() {
        val expectedHours = "0"
        val expectedMinutes = "15"
        val expectedDistance = "4.1"
        val responseMinutes = source.convertResponseToTravelDurationDistance(
            drivingUioToOsloMetTestData, driving
        ).durationMinutes
        val responseHours = source.convertResponseToTravelDurationDistance(
            drivingUioToOsloMetTestData, driving
        ).durationHours
        val responseDistance = source.convertResponseToTravelDurationDistance(
            drivingUioToOsloMetTestData, driving
        ).distance

        assert(
            (expectedDistance == responseDistance && expectedMinutes == responseMinutes && expectedHours == responseHours)
        )

    }

    /*
    Testing that a response containing no routs gets converted properly and displays
    the correct elements.
     */
    @Test
    fun checkDrivingFromNullIsland() {
        val expectedHours = "0"
        val expectedMinutes = "0"
        val expectedDistance = "N/A"

        val responseMinutes = source.convertResponseToTravelDurationDistance(
            drivingNullIslandToOsloMetTestData, driving
        ).durationMinutes
        val responseHours = source.convertResponseToTravelDurationDistance(
            drivingNullIslandToOsloMetTestData, driving
        ).durationHours
        val responseDistance = source.convertResponseToTravelDurationDistance(
            drivingNullIslandToOsloMetTestData, driving
        ).distance

        assert(
            (expectedDistance == responseDistance && expectedMinutes == responseMinutes && expectedHours == responseHours)
        )

    }
}
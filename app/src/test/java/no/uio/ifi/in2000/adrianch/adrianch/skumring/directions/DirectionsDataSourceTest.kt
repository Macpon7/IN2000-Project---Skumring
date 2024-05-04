package no.uio.ifi.in2000.adrianch.adrianch.skumring.directions

import com.google.gson.Gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.directions.DirectionsDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.Directions
import org.junit.Test

class DirectionsDataSourceTest {
    private val gson = Gson()
    private val source = DirectionsDataSource()

    private val walkingUioToOsloMetTestData: Directions = gson.fromJson(
        walkingUioToOsloMet,
        Directions::class.java
    )
    private val cyclingUioToOsloMetTestData: Directions = gson.fromJson(
        cyclingUioToOsloMet,
        Directions::class.java
    )
    private val drivingUioToOsloMetTestData: Directions = gson.fromJson(
        drivingUioToOsloMet,
        Directions::class.java
    )
    private val drivingNullIslandToOsloMetTestData: Directions = gson.fromJson(
        drivingNullIslandToOsloMet,
        Directions::class.java
    )
}
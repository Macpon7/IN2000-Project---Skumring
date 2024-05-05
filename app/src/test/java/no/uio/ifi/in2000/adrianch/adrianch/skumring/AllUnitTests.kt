//package no.uio.ifi.in2000.adrianch.adrianch.skumring
//
//import no.uio.ifi.in2000.adrianch.adrianch.skumring.directions.DirectionsDataSourceTest
//import no.uio.ifi.in2000.adrianch.adrianch.skumring.geocoding.GeoCodingDataSourceTest
//import no.uio.ifi.in2000.adrianch.adrianch.skumring.goldenhourbluehour.GoldenHourBlueHourDataSourceTest
//import no.uio.ifi.in2000.adrianch.adrianch.skumring.locationforecast.LocationForecastDataSourceTest
//import no.uio.ifi.in2000.adrianch.adrianch.skumring.sunrise.SunriseDataSourceTest
//import org.junit.Test
//
//fun main() {
//    val directionsTests = DirectionsDataSourceTest()
//    directionsTests.checkDrivingDurationDistance()
//    directionsTests.checkCyclingDurationDistance()
//    directionsTests.checkWalkingDurationDistance()
//    directionsTests.checkDrivingFromNullIsland()
//
//    val goldenHourBlueHourTests = GoldenHourBlueHourDataSourceTest()
//    goldenHourBlueHourTests.checkNoGoldenOrBlueHour()
//    goldenHourBlueHourTests.checkFixTimeFormat()
//
//    val locationForecastTest = LocationForecastDataSourceTest()
//    locationForecastTest.dataClassesMatchValuesInJSON()
//
//    val sunriseTest = SunriseDataSourceTest()
//    sunriseTest.checkReturntypeFetchSunriseData()
//    sunriseTest.checkdataClassesMatchValuesInJSON()
//}
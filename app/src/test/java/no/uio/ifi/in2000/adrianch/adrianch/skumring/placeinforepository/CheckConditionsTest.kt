package no.uio.ifi.in2000.adrianch.adrianch.skumring.placeinforepository

class CheckConditionsTest {
    /**
     * Testing the function in PlaceInfoRepository that lets us check if
     * the cloud conditions are adequate for photography.
     * Checks up with manually controlled dummy data if it judges correctly
     * according to the agreed upon conditions.
     */
    /*@Test
    suspend fun checkingIfWeatherConditionsAreGood() {
        val gson = Gson()

        val goodWeatherResponse: LocationForecastInfo = gson.fromJson(
            goodWeatherTestData,
            LocationForecastInfo::class.java
        )

        val source = LocationForecastDataSource()
        val goodResult = source.convertResponseToWeatherPerHour(goodWeatherResponse)
        val testRepo = OldPlaceInfoRepositoryImpl()

        val goodBoolean = testRepo.checkConditions(goodResult)
        val goodExpected = true

        assert(goodBoolean == goodExpected)
    }

    @Test
    suspend fun checkingIfWeatherConditionsAreBad() {
        val gson = Gson()

        val badWeatherResponse: LocationForecastInfo = gson.fromJson(
            badWeatherTestData,
            LocationForecastInfo::class.java
        )

        val source = LocationForecastDataSource()
        val badResult = source.convertResponseToWeatherPerHour(badWeatherResponse)
        val testRepo = OldPlaceInfoRepositoryImpl()

        val badBoolean = testRepo.checkConditions(badResult)
        val badExpected = false

        assert(badBoolean == badExpected)
    }*/
}
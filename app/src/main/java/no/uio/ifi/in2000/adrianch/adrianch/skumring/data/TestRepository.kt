package no.uio.ifi.in2000.adrianch.adrianch.skumring.data

import android.util.Log

interface TestRepository {

}

class TestRepositoryImpl: TestRepository {
    init {
        Log.d("TestRepo", "Initialized TestRepo")
    }
}
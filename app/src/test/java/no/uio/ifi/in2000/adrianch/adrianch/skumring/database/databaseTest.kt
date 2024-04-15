package no.uio.ifi.in2000.adrianch.adrianch.skumring.database

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.AppDatabase
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/*
//@RunWith(AndroidJUnit4::class)
class Testclass{
    private lateinit var database: AppDatabase
    private lateinit var dataDao: PlaceInfoDao

    @Before
    fun setUp() {

            Log.d("Testclass", "Creating database...")
            database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
            ).build()
            dataDao = database.placeInfoDao()

    }


    @Test
    fun isright(){
        assertEquals(1,1)
    }

    @After
    fun shutdown(){
        Log.d("Testclass", "Closing database...")
        database.close()
    }
}



 */
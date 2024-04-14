package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlaceInfoEntity::class, ImageEntity::class, ForecastEntity::class], version = 1)
//@TypeConverters(RoomConverters::class)
public abstract class AppDatabase : RoomDatabase() {
    abstract fun placeInfoDao(): PlaceInfoDao
    abstract fun imageDao(): ImageDao
    abstract fun forecastDao(): ForecastDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            Log.d("AppDatabase","Initializes database")
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "placeinfo_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }}

//Old method
/*
    fun buildDatabase(context: Context) : AppDatabase{
        return Room.databaseBuilder(context, AppDatabase::class.java, "placeInfo.db").build()
    }


    @Volatile
    private var INSTANCE : AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase{
        if (INSTANCE == null){
            synchronized(this){
            INSTANCE = buildDatabase(context)}
        }
        return INSTANCE!!
    }

        */
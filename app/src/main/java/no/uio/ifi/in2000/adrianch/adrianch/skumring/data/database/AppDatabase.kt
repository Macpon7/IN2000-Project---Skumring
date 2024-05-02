package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import android.content.Context
import android.util.Log
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val TAG = "AppDatabase"

@Database(
    entities = [PlaceInfoEntity::class, ImageEntity::class, ForecastEntity::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(from = 2, to = 3)]
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeInfoDao(): PlaceInfoDao
    abstract fun imageDao(): ImageDao
    abstract fun forecastDao(): ForecastDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            Log.d(TAG, "Initializes database")
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "placeinfo_database"
                ).createFromAsset("database/placeinfo_database.db").build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
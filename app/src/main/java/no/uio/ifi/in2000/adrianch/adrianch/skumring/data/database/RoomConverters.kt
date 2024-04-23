package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database
import android.net.Uri
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RoomConverters {
    /**
     * Using the built in format functions in LocalDateTime, this converter saves a
     * [LocalDateTime] object as an ISO formatted string
     */
    @TypeConverter
    fun convertLocalDateTimeToString(localDateTime: LocalDateTime): String {
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    /**
     * Using the built in parser in LocalDateTime, this takes in an ISO formatted string and
     * returns a an instance of [LocalDateTime]
     */
    @TypeConverter
    fun convertStringToLocalDateTime(string: String): LocalDateTime {
        return LocalDateTime.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    /**
     * Converts bool to int. True = 1, False = 0
     */
    @TypeConverter
    fun convertBoolToInt(boolean: Boolean): Int {
        return if (boolean) {
            1
        } else {
            0
        }
    }

    /**
     * Converts int to bool. 1 = True, 0 = False
     */
    @TypeConverter
    fun convertIntToBool(int: Int): Boolean {
        // This expression will be true if int==1, and false in every other case
        return int == 1
    }

    /**
     * Converts string to Uri
     */
    @TypeConverter
    fun convertStringtoUri(string: String): Uri {
        return Uri.parse(string)
    }

    /**
     * Converts Uri to String
     */
    @TypeConverter
    fun convertUriToString(uri: Uri): String{
        return uri.toString()
    }
}



package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.Image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.ComponentActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException


//kode for default steder og custom steder
class ImageRepository(private val context: Context) {
    //Note:    //kombinere ID og navn til en string. hvert sted har sin mappe
    data class InternalStoragePhoto(
        val name: String,
        val bml: Bitmap
    )

    //lagre fil som uri

    //loads all pictures at once
    suspend fun loadPhotosFromInternalStorage(): List<InternalStoragePhoto>{
        return withContext(Dispatchers.IO){
            val files = context.filesDir.listFiles()
            //add conditions in line below?
            files.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }.map{
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            } ?: listOf()
        }
    }

    //loadPhoto(placeName: String, id: Int)
    suspend fun loadSinglePhotoFromInternalStorage(fileName: String): InternalStoragePhoto? {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, fileName)
            if (file.exists() && file.canRead() && file.isFile && file.name.endsWith(".jpg")) {
                val bytes = file.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(fileName, bmp)
            } else {
                null
            }
        }
    }

    fun savePhotoToInternalStorage(filename: String, bmp: Bitmap): Boolean {
        return try{
            context.openFileOutput("$filename.jpg", ComponentActivity.MODE_PRIVATE).use{ stream ->
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Couldn't save bitmap")
                }
            }
            true
        } catch(e: IOException){
            e.printStackTrace()
            false
        }
    }

    /*
fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File? {
    // Get the pictures directory that's inside the app-specific directory on
    // external storage.
    val file = File(context.getExternalFilesDir(
        Environment.DIRECTORY_PICTURES), albumName)
    if (!file?.mkdirs()) {
        Log.e("ImageRepository", "Directory not created")
    }
    return file
}

 */

    //filename or pathname as argument?
    fun deletePhotoFromInternalStorage(filename: String): Boolean{
        return try{
            context.deleteFile(filename)
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    fun isDirectoryExists(directoryPath: String): Boolean {
        val directory = File(directoryPath)
        return directory.exists() && directory.isDirectory
    }

    fun checkOrCreateDirectory(placeName: String, ID: Int){
        val directoryName: String = placeName+ID
        if (isDirectoryExists(directoryName)){
            //make directory
        }
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap?{
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun drawableToBitmap(image_name: String): Bitmap? {
        val resourceId = context.resources.getIdentifier(image_name, "drawable", "no.uio.ifi.in2000.adrianch.adrianch.skumring")
        return BitmapFactory.decodeResource(context.resources, resourceId)
    }

    //argument needs to be on format file = "/path/to/image.jpg"
    fun imagepathToBitmap(file:String): Bitmap? {
        val file = File(file)
        val abs_path = file.absolutePath
        return BitmapFactory.decodeFile(abs_path)
    }

    /*
suspend fun createFile(context: Context)
    val file = File(context.filesDir, filename)
*/


}
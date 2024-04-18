package no.uio.ifi.in2000.adrianch.adrianch.skumring

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme
import java.io.File
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkumringTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SkumringApp()
                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.holmenkollen)
                    val success = savePhotoToInternalStorage("example", bitmap)
                    println(success)
                    if (success){
                        Log.d("SavePhoto", "Photo saved successfully")
                    } else {
                        Log.d("SavePhoto", "Photo saved unsuccessfully")
                    }

                    runBlocking {
                        // Call loadPhotosFromInternalStorage within the coroutine scope
                        val photos = loadPhotosFromInternalStorage()
                        // Handle the loaded photos here
                        photos.forEach { photo ->
                            println("Photo name: ${photo.name}")
                            Log.d("SavePhoto", "Photo is called ${photo.name}. Length is ${photos.size}")
                        }
                    }



/*
                }
            }
            internalStoragePhotoAdapter = InternalStoragePhotoAdapter{

            }
            setupInternalStorageRecyclerView()
            loadPhotosFromInternalStorageIntoRecyclerView()
        }*/
    }}}}

                /*
    //private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter
        private fun setupInternalStorageRecyclerView() = binding.rvPrivatePhotos.apply{
        adapter = internalStoragePhotoAdapter
        layoutManager = StaggeredGridLayoutManager(e, RecyclerVire.VERTICAL)
    }*/

                /*
    private fun loadPhotosFromInternalStorageIntoRecyclerView(){
        lifecycleScope.launch {
            val photos = loadPhotosFromInternalStorage()
            internalStoragePhotoAdapter.submitList(photos)
        }
    }
                 */


    //from byte array
   // val byteArray: ByteArray = // Image data in byte array
    //val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)


    //fun drawableToBitmap()
    //    return BitmapFactory.decodeResource(resources, R.drawable.image_name)

    /*
    fun imagepathToBitmap(file:String){
        //file = "/path/to/image.jpg"
        val file = File(file)
        return BitmapFactory.decodeFile(file.absolutePath)
    }

     */



    data class InternalStoragePhoto(
        val name: String,
        val bml: Bitmap
    )

    fun deletePhotoFromInternalStorage(filename: String): Boolean{
        return try{
            deleteFile(filename) //this returns a boolean if the file was successfully deleted or not
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    //loads all pictures at once
    suspend fun loadPhotosFromInternalStorage(): List<InternalStoragePhoto>{
        return withContext(Dispatchers.IO){
            val files = filesDir.listFiles()
            //add conditions in line below?
            files.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }.map{
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            } ?: listOf()
        }
    }

    fun savePhotoToInternalStorage(filename: String, bmp: Bitmap): Boolean {
        return try{
            openFileOutput("$filename.jpg", MODE_PRIVATE).use{ stream ->
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


}
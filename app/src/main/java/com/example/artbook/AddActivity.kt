package com.example.artbook

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.DragStartHelper.OnDragStartListener
import com.example.artbook.databinding.ActivityAddBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private var bitmap:Bitmap? = null

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->
                // Handle the selected file URI
                var file:File = fileFromContentUri(this , uri)
                val filePath: String = file.getPath()
                this.bitmap = BitmapFactory.decodeFile(filePath)
                this.binding.imageView.setImageBitmap(this.bitmap)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        binding = ActivityAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }


    fun selectFile(view : View){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"  // You can specify a MIME type here, such as "image/*" for images
        }
        filePickerLauncher.launch(intent)
    }
    private fun decodeBitmapFromFile(filePath: String): Bitmap? {
        return BitmapFactory.decodeFile(filePath)
    }
    private fun fileFromContentUri(context: Context, contentUri: Uri): File {

        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temporary_file" + if (fileExtension != null) ".$fileExtension" else ""

        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }
    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

     fun save(view:View){
        var model = ArtModel()

        model.bitmapByteArray = bitmapToByteArray(this.bitmap)
        model.year = binding.yearInput.text?.toString()?.toIntOrNull()
        model.artName = binding.artNameInput.text?.toString()
        model.artistName = binding.artistNameInput.text?.toString()

        if(!isValid(model)) {
            Toast.makeText(this@AddActivity, "invalid input data", Toast.LENGTH_SHORT).show()
            return
        }

        // save model to db
         val dbHelper = DatabaseHelper(this)
         val db = dbHelper.writableDatabase

         val values = ContentValues().apply {
             put(ArtTableSchema.COLUMN_NAME_YEAR, model.year)
             put(ArtTableSchema.COLUMN_NAME_ARTNAME, model.artName)
             put(ArtTableSchema.COLUMN_NAME_ARTISTNAME, model.artistName)
             put(ArtTableSchema.COLUMN_NAME_BITMAP, model.bitmapByteArray )
         }
         val newRowId = db?.insert(ArtTableSchema.TABLE_NAME, null, values)

         // navigate
         val intent = Intent(this, MainActivity::class.java)
         startActivity(intent)



     }
    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        if(bitmap == null) return  null
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    fun isValid(model:ArtModel): Boolean {
        return model.artName != null && model.artistName != null && model.year != null && model.bitmapByteArray != null
    }
}
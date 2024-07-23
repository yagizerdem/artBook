package com.example.artbook

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.ContactsContract.Data
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.artbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)





    }

    override fun onStart() {
        super.onStart()
        try{
            val dbHelper:DatabaseHelper = DatabaseHelper(this)
            val db = dbHelper.readableDatabase

            val projection = arrayOf(BaseColumns._ID, ArtTableSchema.COLUMN_NAME_YEAR,  ArtTableSchema.COLUMN_NAME_ARTNAME , ArtTableSchema.COLUMN_NAME_ARTISTNAME ,ArtTableSchema.COLUMN_NAME_BITMAP)
            val cursor = db.query(
                ArtTableSchema.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null            // The sort order
            )

            val listOfModels = ArrayList<ArtModel>()
            with(cursor) {
                while (moveToNext()) {
                    val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val year = getLong(getColumnIndexOrThrow(ArtTableSchema.COLUMN_NAME_YEAR))
                    val  artName = getLong(getColumnIndexOrThrow(ArtTableSchema.COLUMN_NAME_ARTNAME))
                    val artistName = getString(getColumnIndexOrThrow(ArtTableSchema.COLUMN_NAME_ARTISTNAME))
                    val byteArray = getBlob(getColumnIndexOrThrow(ArtTableSchema.COLUMN_NAME_BITMAP))

                    var model = ArtModel()
                    model.year = year.toInt()
                    model.artName = artName.toString()
                    model.artistName = artistName.toString()
                    model.bitmapByteArray = byteArray

                    listOfModels.add(model)
                }
            }
            cursor.close()



            // print arraylist to rcycler view
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfModels.map { it -> "${it.artName} ${it.artistName}" } )
            binding.listView.adapter = adapter

            binding.listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                val selectedModel:ArtModel = listOfModels.get(position)

                val intent = Intent(this, Display::class.java)
                intent.putExtra("selected" , selectedModel )
                startActivity(intent)

            }
        }catch (e:Exception){
            println(e.stackTrace)
        }
    }

    fun newItem(view : View){
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }
}
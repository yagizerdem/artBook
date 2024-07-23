package com.example.artbook

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.artbook.databinding.ActivityDisplayBinding
import com.example.artbook.databinding.ActivityMainBinding

class Display : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display)
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onStart() {
        super.onStart()
        val model = intent.getSerializableExtra("selected") as ArtModel

        binding.textView.text = model.artName.toString()
        binding.textView2.text = model.artistName.toString()
        binding.textView3.text = model.year.toString()
        binding.imageView2.setImageBitmap(BitmapFactory.decodeByteArray(model.bitmapByteArray , 0 , model.bitmapByteArray!!.size))
    }
}
package com.example.artbook

import android.graphics.Bitmap
import java.io.Serializable

data class ArtModel(
    var artistName: String? = null,
    var artName: String? = null,
    var year: Int? = null,
    var bitmapByteArray: ByteArray? = null
) : Serializable {


}
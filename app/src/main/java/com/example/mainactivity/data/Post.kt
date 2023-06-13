package com.example.mainactivity.data

import android.graphics.Bitmap

data class Post (
    var Auteur: String,
    var Content: String,
    var ProfileP: Bitmap,
    var Image: Bitmap? = null,
    var Tag: String,
    var likeCount : Int
) {
}

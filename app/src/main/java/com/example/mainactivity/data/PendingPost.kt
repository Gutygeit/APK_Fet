package com.example.mainactivity.data

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class PendingPost(
    var Auteur: String?, var Content: String?, var ProfileP: Bitmap?,
    var Image:Bitmap? = null, var Tag: String?
)


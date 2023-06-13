package com.example.mainactivity.data

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.semantics.Role

data class Post(
    var Auteur: String?, var Content: String?,var Role: String?, var ProfileP: Bitmap?,
    var Image:Bitmap? = null, var Tag: String?
):
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Auteur)
        parcel.writeString(Content)
        parcel.writeParcelable(Image, flags)
        parcel.writeParcelable(ProfileP, flags)
        parcel.writeString(Tag)
        parcel.writeString(Role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}
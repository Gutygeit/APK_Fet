package com.example.mainactivity.data

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable


/**
 * This class represents a post.
 * @property Auteur The author of the post.
 * @property Content The content of the post.
 * @property Image The image of the post.
 * @property ProfileP The profile picture of the author of the post.
 * @property Tag The tag of the post.
 * @constructor Creates a post.
 * @param Auteur The author of the post.
 * @param Content The content of the post.
 * @param Image The image of the post.
 * @param ProfileP The profile picture of the author of the post.
 * @param Tag The tag of the post.
 */
@Suppress("DEPRECATION")
data class Post(
    var Auteur: String?, var Content: String?,var Role: String?, var ProfileP: Bitmap?,
    var Image:Bitmap? = null, var Tag: String?, var date: String? = null
):


    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString(),
        parcel.readSerializable() as String?

    )

    /**
     * This function is used to write the values of the properties of this object to a parcel.
     * @param parcel The parcel to write the values to.
     * @param flags Additional flags about how the object should be written.
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Auteur)
        parcel.writeString(Content)
        parcel.writeParcelable(Image, flags)
        parcel.writeParcelable(ProfileP, flags)
        parcel.writeString(Tag)
        parcel.writeString(Role)
        parcel.writeSerializable(date)
    }

    /**
     * This function is used to describe the contents of this object.
     * @return The contents of this object.
     */
    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Post> {
        /**
         * This function is used to create a parcel from this object.
         */
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        /**
         * This function is used to create an array of parcels from this object.
         */
        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}
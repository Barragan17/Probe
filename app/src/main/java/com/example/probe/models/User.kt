package com.example.probe.models

import android.os.Parcel
import android.os.Parcelable

data class User (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val mobile: Long = 0,
    val image: String = "",
    val fcmToken: String = "",
    var selected: Boolean = false
    
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readBoolean()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(id)
        writeString(name)
        writeString(email)
        writeLong(mobile)
        writeString(image)
        writeString(fcmToken)
        writeBoolean(selected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
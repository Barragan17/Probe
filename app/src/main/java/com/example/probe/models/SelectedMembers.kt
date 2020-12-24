package com.example.probe.models

import android.os.Parcel
import android.os.Parcelable

data class SelectedMembers(
    val id: String = "",
    val image: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(p0: Parcel, p1: Int)= with(p0) {
        writeString(id)
        writeString(image)
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<SelectedMembers> {
        override fun createFromParcel(parcel: Parcel): SelectedMembers {
            return SelectedMembers(parcel)
        }

        override fun newArray(size: Int): Array<SelectedMembers?> {
            return arrayOfNulls(size)
        }
    }
}
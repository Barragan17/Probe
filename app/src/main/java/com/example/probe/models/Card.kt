package com.example.probe.models

import android.os.Parcel
import android.os.Parcelable

data class Card (
    val name: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),
    val labelColor: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(p0: Parcel, p1: Int) = with(p0) {
        writeString(name)
        writeString(createdBy)
        writeStringList(assignedTo)
        writeString(labelColor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Card> = object : Parcelable.Creator<Card> {
            override fun createFromParcel(source: Parcel): Card = Card(source)
            override fun newArray(size: Int): Array<Card?> = arrayOfNulls(size)
        }
    }
}
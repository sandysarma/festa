package com.example.festa.guest.ui

import android.os.Parcel
import android.os.Parcelable

data class Contact (val displayName: String?, val phoneNumber: String?, val contactId: Long) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(displayName)
        parcel.writeString(phoneNumber)
        parcel.writeLong(contactId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact> {
            TODO("Not yet implemented")
        }
    }
}
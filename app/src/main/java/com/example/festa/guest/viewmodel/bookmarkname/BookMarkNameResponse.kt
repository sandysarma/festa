package com.example.festa.guest.viewmodel.bookmarkname

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BookMarkNameResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("successMessage")
    @Expose
    var successMessage: String? = null

    @SerializedName("collectionName")
    @Expose
    var collectionName: String? = null

    @SerializedName("collectionGuests")
    @Expose
    var collectionGuests: List<CollectionGuest>? = null

    inner class CollectionGuest : Serializable
    {
        @SerializedName("Guest_Name")
        @Expose
        var guestName: String? = null

        @SerializedName("phone_no")
        @Expose
        var phoneNo: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("_id")
        @Expose
        var id: String? = null

    }


}
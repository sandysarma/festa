package com.example.festa.guest.viewmodel.guestlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class GuestListResponse :Serializable{
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("guest_data")
    @Expose
    var guestData: List<GuestDatum>? = null


    inner class GuestDatum: Serializable
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
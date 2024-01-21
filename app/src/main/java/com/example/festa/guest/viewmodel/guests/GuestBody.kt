package com.example.festa.guest.viewmodel.guests

import com.google.gson.annotations.SerializedName

class GuestBody(
    @SerializedName("Guest_Name")
    var Guest_Name: String? = null,
    @SerializedName("phone_no")
    var phone_no:  String? = null
)
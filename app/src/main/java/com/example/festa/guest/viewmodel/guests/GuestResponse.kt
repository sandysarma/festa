package com.example.festa.guest.viewmodel.guests

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GuestResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}
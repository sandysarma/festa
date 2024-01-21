package com.example.festa.view.createevents.viewmodel.addvenue

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class AddVenueResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}
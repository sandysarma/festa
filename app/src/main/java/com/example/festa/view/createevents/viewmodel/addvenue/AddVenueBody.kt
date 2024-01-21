package com.example.festa.view.createevents.viewmodel.addvenue

import com.google.gson.annotations.SerializedName

class AddVenueBody (
    @SerializedName("venue_Name")
    var venue_Name: String? = null,
    @SerializedName("venue_location")
    var venue_location:  String? = null,
    @SerializedName("date")
    var date:  String? = null,
    @SerializedName("start_time")
    var start_time:  String? = null,
    @SerializedName("end_time")
    var end_time:  String? = null,
)
package com.example.festa.view.subeventsupdate.viewmodel.subeventupdate

import com.google.gson.annotations.SerializedName

class SubEventUpdateBody (
    @SerializedName("sub_event_title") var sub_event_title: String,
    @SerializedName("venue_Name") var venue_Name: String,
    @SerializedName("venue_location") var venue_location: String,
    @SerializedName("date") var date: String,
    @SerializedName("start_time") var start_time: String,
    @SerializedName("end_time") var end_time: String,
)

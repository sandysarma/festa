package com.example.festa.view.createevents.viewmodel.createfinal

import com.google.gson.annotations.SerializedName

class CreateFinalEventBody (
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("sub_event_title") var sub_event_title: String? = null,
    @SerializedName("venue_Name") var venue_Name:  String? = null,
    @SerializedName("venue_location") var venue_location:  String? = null,
    @SerializedName("date") var date:  String? = null,
    @SerializedName("start_time") var start_time:  String? = null,
    @SerializedName("end_time") var end_time:  String? = null,
    @SerializedName("event_key") var event_key:  Int? = null
)
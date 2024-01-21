package com.example.festa.view.createevents.viewmodel.getedit

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class GetEditResponses: Serializable {

        @SerializedName("success")
        @Expose
        var success: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("event_venues")
        @Expose
        var eventVenues: List<EventVenue>? = null

    class EventVenue: Serializable {
        @SerializedName("venue_Name")
        @Expose
        var venueName: String? = null

        @SerializedName("venue_location")
        @Expose
        var venueLocation: String? = null

        @SerializedName("date")
        @Expose
        var date: String? = null

        @SerializedName("start_time")
        @Expose
        var startTime: String? = null

        @SerializedName("end_time")
        @Expose
        var endTime: String? = null

        @SerializedName("_id")
        @Expose
        var id: String? = null
    }
    }

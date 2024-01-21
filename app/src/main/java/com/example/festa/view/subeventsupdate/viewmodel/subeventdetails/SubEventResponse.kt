package com.example.festa.view.subeventsupdate.viewmodel.subeventdetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubEventResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("eventId")
    @Expose
    var eventId: String? = null

    @SerializedName("subEvent_Details")
    @Expose
    var subEventDetails: SubEventDetails? = null

    inner class SubEventDetails : Serializable
    {
        @SerializedName("sub_event_title")
        @Expose
        var subEventTitle: String? = null

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
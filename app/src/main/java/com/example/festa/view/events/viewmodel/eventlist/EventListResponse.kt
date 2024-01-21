package com.example.festa.view.events.viewmodel.eventlist
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class EventListResponse : Serializable {

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("events")
    @Expose
    var events: List<Event>? = null

    inner class Event: Serializable
    {
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("userId")
        @Expose
        var userId: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("event_Type")
        @Expose
        var eventType: String? = null

        @SerializedName("images")
        @Expose
        var images: List<String>? = null

        @SerializedName("event_status")
        @Expose
        var eventStatus: Int? = null

        @SerializedName("venue_Date_and_time")
        @Expose
        var venueDateAndTime: List<VenueDateAndTime>? = null

        @SerializedName("co_hosts")
        @Expose
        var coHosts: List<Any>? = null

        @SerializedName("Guests")
        @Expose
        var guests: List<Any>? = null

        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null

        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String? = null

        @SerializedName("__v")
        @Expose
        var v: Int? = null
    }
    inner class VenueDateAndTime: Serializable
    {
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
package com.example.festa.view.events.viewmodel.particularuserlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ParticularUserEventListResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("events")
    @Expose
    var events: List<Event>? = null

    inner class Event : Serializable
    {
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("userId")
        @Expose
        var userId: String? = null

        @SerializedName("userName")
        @Expose
        var userName: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("event_Type")
        @Expose
        var eventType: String? = null

        @SerializedName("co_hosts")
        @Expose
        var coHosts: List<CoHost>? = null

        @SerializedName("Guests")
        @Expose
        var guests: List<Guest>? = null

        @SerializedName("images")
        @Expose
        var images: List<String>? = null

        @SerializedName("event_status")
        @Expose
        var eventStatus: Int? = null

        @SerializedName("venue_Date_and_time")
        @Expose
        var venueDateAndTime: List<VenueDateAndTime>? = null

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
    inner class CoHost : Serializable
    {
        @SerializedName("co_host_Name")
        @Expose
        var coHostName: String? = null

        @SerializedName("phone_no")
        @Expose
        var phoneNo: String? = null

        @SerializedName("_id")
        @Expose
        var id: String? = null
    }

    inner class Guest : Serializable
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

    inner class VenueDateAndTime : Serializable
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

        @SerializedName("sub_event_title")
        @Expose
        var subEventTitle: String? = null

    }
}
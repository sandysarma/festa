package com.example.festa.view.createevents.viewmodel.createevent

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class CreateEventReponse : Serializable {


    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("eventId")
    @Expose
    var eventId: String? = null

}
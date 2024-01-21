package com.example.festa.view.events.viewmodel.createNewAlbum

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreateNewAlbumResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message = ""

    @SerializedName("eventId")
    var eventId = ""

    @SerializedName("album_name")
    var album_name = ""

    @SerializedName("album_id")
    var album_id = ""
}
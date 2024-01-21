package com.example.festa.view.events.viewmodel.album

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AllAlbumResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("successMessage")
    @Expose
    var successMessage: String? = null

    @SerializedName("albums")
    var albums: ArrayList<Albums> = ArrayList()

    inner class Albums : Serializable {
        @SerializedName("album_id")
        var album_id = ""

        @SerializedName("album_name")
        var album_name = ""

        @SerializedName("first_image")
        var first_image = ""
    }
}
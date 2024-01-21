package com.example.festa.view.cohost.viewmodel.addcohost

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class AddCoHostResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}
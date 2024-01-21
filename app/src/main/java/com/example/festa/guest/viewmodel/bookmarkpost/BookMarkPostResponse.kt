package com.example.festa.guest.viewmodel.bookmarkpost

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class BookMarkPostResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}
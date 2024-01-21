package com.example.festa.view.createevents.viewmodel.sendinvite

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendInviteResponse
{
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("successMessage")
    @Expose
    var successMessage: String? = null
}
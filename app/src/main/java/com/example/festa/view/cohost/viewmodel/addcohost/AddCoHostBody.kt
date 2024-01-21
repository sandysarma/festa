package com.example.festa.view.cohost.viewmodel.addcohost

import com.google.gson.annotations.SerializedName

class AddCoHostBody (
    @SerializedName("co_host_Name") var co_host_Name: String,
    @SerializedName("phone_no") var phone_no: String
)


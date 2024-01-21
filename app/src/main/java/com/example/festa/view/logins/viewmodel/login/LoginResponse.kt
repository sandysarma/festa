package com.example.festa.view.logins.viewmodel.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class LoginResponse:Serializable {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
    inner  class  Data:Serializable{
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("fullName")
        @Expose
        var fullName: String? = null

        @SerializedName("phone_no")
        @Expose
        var phoneNo: Long? = null

        @SerializedName("profileImage")
        @Expose
        var profileImage: String? = null

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
}
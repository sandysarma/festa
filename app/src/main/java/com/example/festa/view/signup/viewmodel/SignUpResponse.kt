package com.example.festa.view.signup.viewmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class SignUpResponse : Serializable{

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("user_details")
    @Expose
    var userDetails: UserDetails? = null
    inner class UserDetails :Serializable
    {

        @SerializedName("fullName")
        @Expose
        var fullName: String? = null

        @SerializedName("phone_no")
        @Expose
        var phoneNo: Long? = null

        @SerializedName("profileImage")
        @Expose
        var profileImage: String? = null

        @SerializedName("userId")
        @Expose
        var userId: String? = null

        @SerializedName("user_status")
        @Expose
        var userStatus: Int? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

    }

}
package com.example.festa.view.profile.viewmodel.getprofile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class GetProfileResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("user_details")
    @Expose
    var userDetails: UserDetails? = null

    inner  class UserDetails : Serializable
    {

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

        @SerializedName("user_status")
        @Expose
        var userStatus: Int? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

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
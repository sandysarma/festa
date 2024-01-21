package com.example.festa.view.cohost.viewmodel.cohostlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CostHostListResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("co_hostsData")
    @Expose
    var coHostsData: List<CoHostsDatum>? = null

    inner class CoHostsDatum : Serializable {
        @SerializedName("co_host_Name")
        @Expose
        var coHostName: String? = null

        @SerializedName("phone_no")
        @Expose
        var phoneNo: String? = null

        @SerializedName("_id")
        @Expose
        var id: String? = null
    }
}
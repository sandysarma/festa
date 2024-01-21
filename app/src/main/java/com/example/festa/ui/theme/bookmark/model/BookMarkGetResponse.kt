package com.example.festa.ui.theme.bookmark.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BookMarkGetResponse:Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("success_message")
    @Expose
    var successMessage: String? = null

    @SerializedName("allCollections")
    @Expose
    var allCollections: List<AllCollection>? = null

    inner class AllCollection: Serializable{

        @SerializedName("collection_id")
        @Expose
        var collectionId: String? = null

        @SerializedName("collection_name")
        @Expose
        var collectionName: String? = null

        @SerializedName("collection_created_date")
        @Expose
        var collectionCreatedDate: String? = null

        @SerializedName("collection_entries_count")
        @Expose
        var collectionEntriesCount: Int? = null

    }
}
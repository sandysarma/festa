package com.example.festa.view.profile.viewmodel.feedback

import com.google.gson.annotations.SerializedName

class FeedBackBody (
    @SerializedName("rating") var ratings: String,
    @SerializedName("message") var messages: String,
    @SerializedName("feedback_Type") var feedbackType:  String
)

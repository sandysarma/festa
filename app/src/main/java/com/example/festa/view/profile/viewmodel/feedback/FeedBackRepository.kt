package com.example.festa.view.profile.viewmodel.feedback

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class FeedBackRepository @Inject constructor(private val apiService: ApiServices){
    suspend fun getfeedback(
        userId:String,
        eventId:String,
        body: FeedBackBody

    ): Observable<FeedBackResponse> {
        return apiService.feedback(
            userId,
            eventId,
            body
        )
    }


}
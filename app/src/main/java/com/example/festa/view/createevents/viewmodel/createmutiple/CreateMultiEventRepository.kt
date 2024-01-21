package com.example.festa.view.createevents.viewmodel.createmutiple

import com.example.festa.services.ApiServices
import com.example.festa.view.createevents.viewmodel.sendinvite.SendInviteResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateMultiEventRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getCreateMultiEvent(
        eventId: String,
        title: RequestBody,
        description: RequestBody,
        event_Type: RequestBody,
        venue_Date_and_time: RequestBody,
        event_key: RequestBody,
        identify_document: Array<MultipartBody.Part?>

    ): Observable<SendInviteResponse> {
        return apiService.createMultipleEvent(
            eventId,
            title,
            description,
            event_Type,
            venue_Date_and_time,
            event_key,
            identify_document
        )
    }
}
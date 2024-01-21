package com.example.festa.view.createevents.viewmodel.createfinal

import com.example.festa.services.ApiServices
import com.example.festa.view.createevents.viewmodel.sendinvite.SendInviteResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateFinalEvenRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getCreateFinalEvent(
        eventId: String,
        title: RequestBody,
        description: RequestBody,
        event_Type: RequestBody,
        venue_Name: RequestBody,
        venue_location: RequestBody,
        date: RequestBody,
        start_time: RequestBody,
        end_time: RequestBody,
        event_key: RequestBody,
        identify_document: Array<MultipartBody.Part?>

    ): Observable<SendInviteResponse> {
        return apiService.createFinalEvent(
            eventId,
            title,
            description,
            event_Type,
            venue_Name,
            venue_location,
            date,
            start_time,
            end_time,
            event_key,
            identify_document
        )
    }
    }
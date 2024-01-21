package com.example.festa.view.createevents.viewmodel.createevent

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateEventRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getCreateEvent(
        userId: String,
        title: RequestBody,
        description: RequestBody,
        event_Type: RequestBody,
        venue_Date_and_time: RequestBody,
        identify_document: Array<MultipartBody.Part?>

    ): Observable<CreateEventReponse>
    {
        return apiService.createEvent(
            userId,
            title,
            description,
            event_Type,
            venue_Date_and_time,
            identify_document)
    }
}
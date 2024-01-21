package com.example.festa.view.events.viewmodel.eventlist

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class EventListRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getEventList(userId: String): Observable<EventListResponse>
    {
        return apiService.getEventList(userId)
    }
}
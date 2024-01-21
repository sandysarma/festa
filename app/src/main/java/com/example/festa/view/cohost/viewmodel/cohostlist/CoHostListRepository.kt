package com.example.festa.view.cohost.viewmodel.cohostlist


import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class CoHostListRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getGuestList(eventId: String): Observable<CostHostListResponse> {
        return apiService.getCoHostGuests(eventId)
    }
}
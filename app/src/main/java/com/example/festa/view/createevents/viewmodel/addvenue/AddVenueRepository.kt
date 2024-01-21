package com.example.festa.view.createevents.viewmodel.addvenue

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class AddVenueRepository @Inject constructor(
    private val apiServices: ApiServices) {
    suspend fun addNewVenue( userId: String,body: AddVenueBody): Observable<AddVenueResponse> {
        return apiServices.addNewVenue(userId,body)}
}
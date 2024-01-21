package com.example.festa.view.subeventsupdate.viewmodel.subeventdetails

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class SubEventGetRepository  @Inject constructor(private val apiService: ApiServices) {
    suspend fun getSubEventDetails(guestId:String,eventId: String): Observable<SubEventResponse>
    {
        return apiService.deleteSubEventDetails(guestId,eventId)
    }
}
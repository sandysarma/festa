package com.example.festa.view.createevents.viewmodel.getedit

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class GetEditRepository  @Inject constructor(private val apiService: ApiServices) {
    suspend fun getEdit(eventId: String): Observable<GetEditResponse>
    {
        return apiService.getEdit(eventId)
    }
}
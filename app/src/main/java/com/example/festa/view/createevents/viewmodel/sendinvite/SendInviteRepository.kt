package com.example.festa.view.createevents.viewmodel.sendinvite

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class SendInviteRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun getInvite(eventId: String): Observable<SendInviteResponse> {
        return apiServices.sendInvite(eventId)
    }
}
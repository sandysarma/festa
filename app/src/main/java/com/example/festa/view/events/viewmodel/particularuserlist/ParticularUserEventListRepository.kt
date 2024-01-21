package com.example.festa.view.events.viewmodel.particularuserlist

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class ParticularUserEventListRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getEventList(userId: String): Observable<ParticularUserEventListResponse>
    {
        return apiService.getEventParticularList(userId)
    }
}
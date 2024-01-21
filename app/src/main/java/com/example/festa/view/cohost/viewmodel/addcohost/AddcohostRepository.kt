package com.example.festa.view.cohost.viewmodel.addcohost

import com.example.festa.guest.viewmodel.guests.GuestResponse
import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class AddcohostRepository  @Inject constructor(private val apiService: ApiServices){
    suspend fun getaddcohost(eventId:String, body: AddCoHostBody): Observable<GuestResponse> {
        return apiService.addCoHost(eventId, body)
    }


}
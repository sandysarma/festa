package com.example.festa.guest.viewmodel.guests
import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class GuestRepository@Inject constructor(
    private val apiServices: ApiServices
) {
    suspend fun addGuest( eventId: String,body: GuestBody): Observable<GuestResponse> {
        return apiServices.addGuest(eventId,body)}
}
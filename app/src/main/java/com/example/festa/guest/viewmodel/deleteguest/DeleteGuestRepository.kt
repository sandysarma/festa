package com.example.festa.guest.viewmodel.deleteguest

import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostResponse
import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class DeleteGuestRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getDelete(eventId: String,guestId:String): Observable<BookMarkPostResponse>
    {
        return apiService.deleteGuest(eventId,guestId)
    }
}
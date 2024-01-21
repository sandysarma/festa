package com.example.festa.view.createevents.viewmodel.deletesubevent

import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostResponse
import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class DeleteSubEventRepository  @Inject constructor(private val apiService: ApiServices) {
    suspend fun getDeleteCoHost(guestId:String,eventId: String): Observable<BookMarkPostResponse>
    {
        return apiService.deleteMultipleEvent(guestId,eventId)
    }
}
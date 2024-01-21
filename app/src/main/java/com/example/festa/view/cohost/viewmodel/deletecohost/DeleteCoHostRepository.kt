package com.example.festa.view.cohost.viewmodel.deletecohost

import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostResponse
import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class DeleteCoHostRepository  @Inject constructor(private val apiService: ApiServices) {
    suspend fun getDeleteCoHost(guestId:String,eventId: String): Observable<BookMarkPostResponse>
    {
        return apiService.deleteCoHost(guestId,eventId)
    }
}
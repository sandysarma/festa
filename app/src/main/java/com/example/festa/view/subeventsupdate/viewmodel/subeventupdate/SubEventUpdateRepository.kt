package com.example.festa.view.subeventsupdate.viewmodel.subeventupdate

import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostResponse
import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class SubEventUpdateRepository  @Inject constructor(private val apiService: ApiServices){
    suspend fun getUpdateSubEvent(venueId:String,eventId : String, body: SubEventUpdateBody): Observable<BookMarkPostResponse> {
        return apiService.updateSubEvent(venueId,eventId, body)
    }


}
package com.example.festa.guest.viewmodel.bookmarkpost

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class BookMarkPostRepository @Inject constructor(private val apiService: ApiServices){
    suspend fun getBookMark(eventId:String, body: BookMarkPostBody): Observable<BookMarkPostResponse> {
        return apiService.bookMark(eventId, body)
    }


}
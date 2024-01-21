package com.example.festa.ui.theme.bookmark.model

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class BookMarkGetRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getBookMarkList(eventId: String): Observable<BookMarkGetResponse>
    {
        return apiService.getBookMark(eventId)
    }
}
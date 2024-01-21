package com.example.festa.ui.theme.collectguestlist.deleteguest

import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostResponse
import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class CollectionGuestDeleteRepository@Inject constructor(private val apiService: ApiServices) {
    suspend fun getDelete(collection_id: String,guestId:String): Observable<BookMarkPostResponse>
    {
        return apiService.deleteGuestBook(collection_id,guestId)
    }
}
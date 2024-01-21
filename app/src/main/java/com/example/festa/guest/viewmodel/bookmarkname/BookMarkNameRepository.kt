package com.example.festa.guest.viewmodel.bookmarkname

import com.example.festa.services.ApiServices
import com.example.festa.ui.theme.bookmark.model.BookMarkGetResponse
import io.reactivex.Observable
import javax.inject.Inject

class BookMarkNameRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getBookMarkList(collectionId: String): Observable<BookMarkNameResponse>
    {
        return apiService.getCollectionGuest(collectionId)
    }
}
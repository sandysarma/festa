package com.example.festa.view.events.viewmodel.createNewAlbum

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class CreateNewAlbumReporsitory @Inject constructor(private val apiService: ApiServices) {
    suspend fun createNewAlbums(eventId: String, paramObject: CreateAlbumBody): Observable<CreateNewAlbumResponse> {
        return apiService.createNewAlbum(eventId,paramObject)
    }
}
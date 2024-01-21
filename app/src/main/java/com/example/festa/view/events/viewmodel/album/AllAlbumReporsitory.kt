package com.example.festa.view.events.viewmodel.album

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class AllAlbumReporsitory @Inject constructor(private val apiService: ApiServices) {
    suspend fun getAllAlbumList(eventId: String): Observable<AllAlbumResponse> {
        return apiService.getAllAlbum(eventId)
    }
}
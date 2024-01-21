package com.example.festa.guest.Uploadexcelapi

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadexcelRepository@Inject constructor(
    private val apiServices: ApiServices)
{
    suspend fun getUploadExcel(
        eventId: String,
        identify_document: MultipartBody.Part

    ): Observable<UploadexcelResponse> {
        return apiServices.uploadExcel(
            eventId,
            identify_document
        )
    }

}
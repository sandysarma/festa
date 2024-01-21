package com.example.festa.view.profile.viewmodel.getprofile

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class GetProfileRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getUserDetails(userId: String): Observable<GetProfileResponse> {
        return apiService.getProfileDetails(userId)
    }
}
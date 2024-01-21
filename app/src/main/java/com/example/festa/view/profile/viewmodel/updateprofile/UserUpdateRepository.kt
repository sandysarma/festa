package com.example.festa.view.profile.viewmodel.updateprofile

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserUpdateRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun updateProfile(
        userId: String,
        fullName: RequestBody,
        phone_no: RequestBody,
        email: RequestBody,
        profileImage: MultipartBody.Part
    ): Observable<UserUpdateResponse> {
        return apiService.updateUserDetails(
            userId,
            fullName,
            phone_no,
            email,
            profileImage
        )
    }
}
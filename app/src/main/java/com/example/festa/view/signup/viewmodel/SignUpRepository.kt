package com.example.festa.view.signup.viewmodel

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SignUpRepository @Inject constructor(private val apiService: ApiServices){
    suspend fun getUserSignUp(
        fullName: RequestBody,
        phone_no: RequestBody,
        identify_document: MultipartBody.Part

    ): Observable<SignUpResponse> {
        return apiService.customerRegister(
            fullName,
            phone_no,
            identify_document
        )
    }


}